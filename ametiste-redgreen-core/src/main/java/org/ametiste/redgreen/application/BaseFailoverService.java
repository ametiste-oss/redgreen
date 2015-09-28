package org.ametiste.redgreen.application;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.bundle.Bundle;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     Base implementation of the {@link FailoverService}.
 * </p>
 *
 * @since 0.1.1
 */
public class BaseFailoverService implements FailoverService {

    private final RedgreenBundleRepostitory bundleRepostitory;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseFailoverService(RedgreenBundleRepostitory bundleRepostitory) {
        this.bundleRepostitory = bundleRepostitory;
    }

    @Override
    public void performRequest(RedgreenRequest rgRequest, RedgreenResponse rgResponse) throws RedgreenBundleDoesNotExist {

        final Bundle bundle = bundleRepostitory.loadBundle(rgRequest.targetBundle());
        assert bundle != null;

        bundle.execute(rgRequest, rgResponse);
    }

}
