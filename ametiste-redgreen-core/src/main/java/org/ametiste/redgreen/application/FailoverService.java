package org.ametiste.redgreen.application;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;

/**
 *
 * @since 0.1.1
 */
public interface FailoverService {

    void performRequest(RedgreenRequest rgRequest, RedgreenResponse rgResponse)
            throws RedgreenBundleDoesNotExist;

}
