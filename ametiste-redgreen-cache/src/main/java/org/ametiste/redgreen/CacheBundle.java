package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.Bundle;

import java.io.OutputStream;

/**
 *
 * @since
 */
public interface CacheBundle extends Bundle {

    void cacheResponse(RedgreenRequest request, ForwardedResponse response);

}
