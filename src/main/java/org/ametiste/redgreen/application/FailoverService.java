package org.ametiste.redgreen.application;

import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenRequest;

/**
 *
 * @since 0.1.1
 */
public interface FailoverService {

    <T> T performRequest(RedgreenRequest rgRequest, RedgreenResponse<T> rgResponse)
            throws RedgreenBundleDoesNotExist;

}
