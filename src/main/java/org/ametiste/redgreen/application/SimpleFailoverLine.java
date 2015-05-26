package org.ametiste.redgreen.application;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.ametiste.redgreen.data.RedgreenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.ResourceHttpMessageConverter;
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
 *     Note, {@code Hystrix} circuit breaker is disabled for commands used to execute methods
 *     of this implementation.
 * </p>
 *
 * <p>
 *     Command execution timeout can be modified using application
 *     property <i>redgreen.simpleFailoverLine.commandTimeout</i> ( note, a value should be defined in millis ).
 * </p>
 *
 * @since 0.1.0
 */
public class SimpleFailoverLine implements FailoverLine {

    // NOTE: ResourceHttpMessageConverter used to handle all possible content-types
    private RestTemplate restTemplate = new RestTemplate(
            Arrays.asList(new ResourceHttpMessageConverter())
    );

    private final RedgreenBundleRepostitory bundleRepostitory;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SimpleFailoverLine(RedgreenBundleRepostitory redgreenBundleRepostitory) {
        this.bundleRepostitory = redgreenBundleRepostitory;
    }

    @HystrixCommand(fallbackMethod = "performFallback",
            commandProperties = {
                @HystrixProperty(name="circuitBreaker.enabled", value="false"),
                @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="${redgreen.simpleFailoverLine.commandTimeout}")
            }
    )
    @Override
    public Object performRequest(String bundleName, Supplier<RedgreenRequest> requestSupplier) {

        // TODO: add excpetion and existens check
        // TODO: atm repository exception triggering failover
        final RedgreenBundle redgreenBundle = bundleRepostitory.loadBundle(bundleName);

        return doResourceRequest(redgreenBundle.greenResource(), requestSupplier);
    }

    public Object performFallback(String bundleName, Supplier<RedgreenRequest> requestSupplier) {
        final RedgreenBundle redgreenBundle = bundleRepostitory.loadBundle(bundleName);

        return redgreenBundle.mapRedResources((r) -> doSafeResourceRequest(r, requestSupplier))
                .filter((r) -> r != null)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Object doResourceRequest(String resource, Supplier<RedgreenRequest> requestSupplier) {

        final RedgreenRequest rr = requestSupplier.get();

        // TODO: Need to find a way to form resourceURI+queryString pair
        // NOTE: ResourceHttpMessageConverter used to handle all possible content-types
        return restTemplate.exchange(resource + "?" + rr.getQueryString(), rr.getHttpMethod(),
                HttpEntity.EMPTY, Resource.class);
    }

    private Object doSafeResourceRequest(String resource, Supplier<RedgreenRequest> requestSupplier) {
        try {
            return doResourceRequest(resource, requestSupplier);
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("Resource failed: " + resource);
            }
            return null;
        }
    }

}
