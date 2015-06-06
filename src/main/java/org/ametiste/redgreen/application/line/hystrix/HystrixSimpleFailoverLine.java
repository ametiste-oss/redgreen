package org.ametiste.redgreen.application.line.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.ametiste.redgreen.application.bundle.RedgreenPair;
import org.ametiste.redgreen.application.request.ResourceRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.ametiste.redgreen.data.RedgreenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @HystrixCommand(commandKey=HYSTRIX_COMMAND_KEY, fallbackMethod = "performFallback")
    @Override
    public <T> T performRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse<T> rgResponse)
            throws RedgreenBundleDoesNotExist {

        return requestDriver.executeStrictRequest(
                createResourceRequest(rgRequest, resourcesPair, resourcesPair.getGreen()),
                rgResponse
        );
    }

    private ResourceRequest createResourceRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair, String green) {
        return new ResourceRequest(rgRequest,
                green,
                resourcesPair.getcTimeout(),
                resourcesPair.getrTimeout()
        );
    }

    public <T> T performFallback(RedgreenRequest rgRequest, RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse<T> rgResponse) {

        if (logger.isErrorEnabled()) {
            logger.error("Green resource failed, performing fallback for: " + resourcesPair.getName());
        }

        return resourcesPair.getRed().stream().map((r) -> requestDriver
                .executeSafeRequest(createResourceRequest(rgRequest, resourcesPair, r), rgResponse))
                .filter((r) -> r != null)
                .findFirst()
                // TODO: define specific exception
                .orElseThrow(RuntimeException::new);
    }

}
