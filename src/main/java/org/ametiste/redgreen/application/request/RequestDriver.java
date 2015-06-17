package org.ametiste.redgreen.application.request;

import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenRequest;

/**
 *
 * @since 0.1.1
 */
public interface RequestDriver {

    void executeStrictRequest(ResourceRequest rgRequest, RedgreenResponse redgreenResponse);

    void executeSafeRequest(ResourceRequest rgRequest, RedgreenResponse redgreenResponse);

}
