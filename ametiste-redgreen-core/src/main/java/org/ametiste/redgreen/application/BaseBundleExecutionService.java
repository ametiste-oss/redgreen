package org.ametiste.redgreen.application;

import org.ametiste.metrics.annotations.ErrorCountable;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.bundle.Bundle;
import org.ametiste.redgreen.application.response.RedgreenResponse;
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
    @Timeable(name="execuiton.line.base.perform.timing")
    @ErrorCountable(name = "execuiton.line.base.perform.general-errors")
    // TODO: Add specifica metric after ame-metrics support for multiple annotation would be added
    // @ErrorCountable(name = "execuiton.line.base.perform.bundle-access-error", exceptionClass = RedgreenBundleDoesNotExist.class)
    public void performRequest(RedgreenRequest rgRequest, RedgreenResponse rgResponse) throws RedgreenBundleDoesNotExist {

        final Bundle bundle = bundleRepostitory.loadBundle(rgRequest.targetBundle());
        assert bundle != null;

        bundle.execute(rgRequest, rgResponse);
    }

}
