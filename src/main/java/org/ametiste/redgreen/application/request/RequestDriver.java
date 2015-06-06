package org.ametiste.redgreen.application.request;

import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenRequest;

/**
 *
 * @since 0.1.1
 */
public interface RequestDriver {

    <T> T executeStrictRequest(ResourceRequest rgRequest, RedgreenResponse<T> redgreenResponse);

    <T> T executeSafeRequest(ResourceRequest rgRequest, RedgreenResponse<T> redgreenResponse);

}
