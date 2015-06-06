package org.ametiste.redgreen.application;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.ametiste.redgreen.data.RedgreenRequest;
import org.ametiste.redgreen.interfaces.ForwardedResponseMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * <p>
 *  Failover line that uses {@link RedgreenBundleRepostitory} to lookup resources
 *  bundle.
 * </p>
 *
 * <p>
 *     This line implemented using {@code Hystrix} commands, each request line is
 *     composed by two parts - <i>command</i> and <i>fallback</i>.
 * </p>
 *
 * <p>
 *  A <i>green</i> resource of a bundle would be used as target to execute <i>command</i>
 *  part of line, and if any error was accured, each <i>red</i> resource would be invoked
 *  one by one as <i>failure</i> part of line.
 * </p>
 *
 * <p>
 *     Note, all <i>red</i> resources would be executed in the same {@code Hystrix Fallback Command},
 *     so the one thread/symaphore will be used.
 * </p>
 *
 * <p>
 *     Note, {@code Hystrix} circuit breaker is disabled for commands used to execute methods
 *     of this implementation.
 * </p>
 *
 * @since 0.1.0
 */
public class HystrixSimpleFailoverLine implements FailoverLine {

    /**
     * Defines {@code Hystrix} command key, this constant can be used as the reference
     * to this line execution command.
     *
     * @since 0.1.0
     */
    public static final String HYSTRIX_COMMAND_KEY = "SimpleFailoverLineExecution";

    // NOTE: ResourceHttpMessageConverter used to handle all possible content-types
    // private RestTemplate restTemplate = new RestTemplate(
    //        Arrays.asList(new ForwardedResponseMessageConverter())
    //);

    private final RedgreenBundleRepostitory bundleRepostitory;

    private final RequestExecutor requestExecutor;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public HystrixSimpleFailoverLine(RequestExecutor requestExecutor,
                                     RedgreenBundleRepostitory redgreenBundleRepostitory) {
        this.requestExecutor = requestExecutor;
        this.bundleRepostitory = redgreenBundleRepostitory;
    }

    @HystrixCommand(commandKey=HYSTRIX_COMMAND_KEY, fallbackMethod = "performFallback")
    @Override
    public <T> T performRequest(Supplier<RedgreenRequest> requestSupplier, Forwarder<T> forwarder)
            throws RedgreenBundleDoesNotExist {

        final RedgreenBundle redgreenBundle =
                loadTargetBundle(requestSupplier);

        return doResourceRequest(
                redgreenBundle.greenResource(),
                requestSupplier.get(),
                forwarder
        );
    }

    public <T> T performFallback(Supplier<RedgreenRequest> requestSupplier, Forwarder<T> forwarder) {

        // NOTE: this method performs fallback operations, to avhieve execution over the
        // targeted bundle wee need to load bundle again.
        final RedgreenBundle redgreenBundle = loadTargetBundle(requestSupplier);

        if (logger.isErrorEnabled()) {
            logger.error("Green resource failed, performing fallback for: " + redgreenBundle.name());
        }

        return redgreenBundle.mapRedResources((r) -> doSafeResourceRequest(r, requestSupplier.get(), forwarder))
                .filter((r) -> r != null)
                .findFirst()
                // TODO: define specific exception
                .orElseThrow(RuntimeException::new);
    }

    private <T> T doResourceRequest(String resource, RedgreenRequest request, Forwarder<T> forwarder) {
        // TODO: Need to find a way to form resourceURI+queryString pair

        try {
            return requestExecutor.executeRequest(
                    request.requestMethod(),
                    resource + "?" + request.requestQuery(),
                    forwarder
            );
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resource reading error", e);
            }
            throw e;
        }
    }

    private <T> T doSafeResourceRequest(String resource, RedgreenRequest request, Forwarder<T> forwarder) {
        try {
            return doResourceRequest(resource, request ,forwarder);
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("Red resource failed: " + resource);
                logger.debug("With error: ", e);
            }
            return null;
        }
    }

    private RedgreenBundle loadTargetBundle(Supplier<RedgreenRequest> requestSupplier) {
        // TODO: add excpetion and existens check
        // TODO: atm repository exception triggering failover
        return bundleRepostitory.loadBundle(requestSupplier.get().targetBundle());
    }

}
