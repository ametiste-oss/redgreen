package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.application.response.RedgreenResponse;

import java.io.IOException;
import java.io.OutputStream;
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
    public void forward(Map<String, List<String>> headers, ForwardedResponse body) {
        cacheBundle.cacheResponse(request, body);
    }

}
