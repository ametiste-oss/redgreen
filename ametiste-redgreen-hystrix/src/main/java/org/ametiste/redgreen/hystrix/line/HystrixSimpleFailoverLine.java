package org.ametiste.redgreen.hystrix.line;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.RedgreenPair;
import org.ametiste.redgreen.hystrix.configuration.HystrixSimpleFailoverLineConfiguration;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.ResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  This {@code line} implemented using {@code Hystrix} commands framework, each request line is
 *  composed by two parts - <i>command</i> and <i>fallback</i>.
 * </p>
 *
 * <p>
 *  A <i>green</i> resource of a bundle would be used as target to execute <i>command</i>
 *  part of line, and if any error was occured, each <i>red</i> resource would be tried
 *  one by one as <i>failure</i> part of line.
 * </p>
 *
 * <p>
 *  Note, all <i>red</i> resources would be executed in the same {@code Hystrix Fallback Command},
 *  so the one thread/symaphore will be used.
 * </p>
 *
 * <p>
 *  Note, {@code Hystrix} circuit breaker behaviour is controlled global for each {@code line}
 *  instance. See {@link HystrixSimpleFailoverLineConfiguration} for details.
 * </p>
 *
 * @see HystrixSimpleFailoverLineConfiguration
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
    public void performRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse) {
        requestDriver.executeRequest(
                createResourceRequest(rgRequest, resourcesPair, resourcesPair.getGreen()),
                rgResponse
        );
    }

    // TODO: I guess RedgreenPair should have method execute to doing this
    private ResourceRequest createResourceRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair, String green) {
        return new ResourceRequest(rgRequest,
                green,
                resourcesPair.getcTimeout(),
                resourcesPair.getrTimeout()
        );
    }

    public void performFallback(RedgreenRequest rgRequest, RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse) {

        if (logger.isDebugEnabled()) {
            logger.debug("Green resource failed, performing fallback for: {}", resourcesPair.getName());
        }

        boolean isFailed = true;

        for (String red : resourcesPair.getRed()) {
            try {
                requestDriver.executeRequest(
                        createResourceRequest(rgRequest, resourcesPair, red), rgResponse);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error during fallback execution.", e);
                }
                continue;
            }
            isFailed = false;
            break;
        }

        if (isFailed) {
            throw new RuntimeException("Resource fallback failed.");
        }

    }

}
