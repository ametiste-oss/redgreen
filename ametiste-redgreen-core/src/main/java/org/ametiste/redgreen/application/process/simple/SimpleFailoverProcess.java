package org.ametiste.redgreen.application.process.simple;

import org.ametiste.metrics.annotations.Countable;
import org.ametiste.metrics.annotations.ErrorCountable;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.process.FailoverProcess;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.ResourceRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.RedgreenPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @since 0.3.0
 */
public class SimpleFailoverProcess implements FailoverProcess {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Timeable(name= SimpleFailoverProcessorMetrics.MAIN_PERF_TIMING)
    @Countable(name= SimpleFailoverProcessorMetrics.MAIN_EVENT_ATTEMPTS)
    @ErrorCountable(name= SimpleFailoverProcessorMetrics.MAIN_EVENT_ERRORS)
    public void performMain(RedgreenRequest rgRequest, RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse) {
        requestDriver.executeRequest(
                createResourceRequest(rgRequest, resourcesPair, resourcesPair.getGreen()),
                rgResponse
        );
    }

    @Override
    @Timeable(name= SimpleFailoverProcessorMetrics.FAILOVER_PERF_TIMING)
    @Countable(name= SimpleFailoverProcessorMetrics.FAILOVER_EVENT_ATTEMPTS)
    @ErrorCountable(name= SimpleFailoverProcessorMetrics.FAILOVER_EVENT_ERRORS)
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

    // TODO: I guess RedgreenPair should have method execute to doing this
    private ResourceRequest createResourceRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair, String green) {
        return new ResourceRequest(rgRequest,
                green,
                resourcesPair.getcTimeout(),
                resourcesPair.getrTimeout()
        );
    }

}
