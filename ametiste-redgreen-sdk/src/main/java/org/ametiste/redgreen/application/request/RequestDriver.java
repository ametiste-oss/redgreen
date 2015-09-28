package org.ametiste.redgreen.application.request;

import org.ametiste.redgreen.application.response.RedgreenResponse;

/**
 *
 * @since 0.1.1
 */
public interface RequestDriver {

    void executeRequest(ResourceRequest rgRequest, RedgreenResponse redgreenResponse);

}
