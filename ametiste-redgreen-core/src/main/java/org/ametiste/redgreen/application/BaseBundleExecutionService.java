package org.ametiste.redgreen.application;

import org.ametiste.metrics.annotations.ErrorCountable;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.Bundle;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     Base implementation of the {@link BundleExecutionService}.
 * </p>
 *
 * @since 0.1.1
 */
public class BaseBundleExecutionService implements BundleExecutionService {

    private final RedgreenBundleRepostitory bundleRepostitory;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseBundleExecutionService(RedgreenBundleRepostitory bundleRepostitory) {
        this.bundleRepostitory = bundleRepostitory;
    }

    @Override
    @Timeable(name= ExecutionLineMetric.PERFORM_TIMING)
    @ErrorCountable(name = ExecutionLineMetric.FAILOVER_GENERAL_ERRORS)
    // TODO: Add specific metric after ame-metrics support for multiple annotation would be added
    // @ErrorCountable(name = "execuiton.line.base.perform.bundle-access-error", exceptionClass = RedgreenBundleDoesNotExist.class)
    public void performRequest(RedgreenRequest rgRequest, RedgreenResponse rgResponse) throws RedgreenBundleDoesNotExist {

        final Bundle bundle = bundleRepostitory.loadBundle(rgRequest.targetBundle());
        assert bundle != null;

        logger.debug("Executing request bundle: {}", bundle.name());

        try {
            bundle.execute(rgRequest, rgResponse);
        } catch (Exception e) {
            logger.debug("Executing error bundle. Request bundle failed: {} ", bundle.name());
            executeErrorBundle(rgRequest, rgResponse, e);
            logger.debug("Error bundle execution done.");
        }
    }

    private void executeErrorBundle(RedgreenRequest rgRequest, RedgreenResponse rgResponse, Exception e) {
        bundleRepostitory.loadErrorBundle(rgRequest.targetBundle(), e)
                .execute(rgRequest, rgResponse);
    }

}
