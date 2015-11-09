package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public class CacheLineResponse implements RedgreenResponse {

    private final RedgreenRequest request;
    private final CacheBundle cacheBundle;

    public CacheLineResponse(RedgreenRequest request, CacheBundle cacheBundle) {
        this.request = request;
        this.cacheBundle = cacheBundle;
    }

    @Override
    public void sendRedirect(String redirect) {

    }

    @Override
    public void attachHeaders(Map headers) {

    }

    @Override
    public void attachBody(Object body) {
        // cacheBundle.cacheResponse(request, body);
    }

}
