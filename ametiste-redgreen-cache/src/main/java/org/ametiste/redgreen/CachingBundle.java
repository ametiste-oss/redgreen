package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.line.ExecutionLineException;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.Bundle;

/**
 *
 * @since
 */
public class CachingBundle implements Bundle {

    private final Bundle dataBundle;

    private final CacheBundle cacheBundle;

    public CachingBundle(Bundle dataBundle, CacheBundle cacheBundle) {
        this.dataBundle = dataBundle;
        this.cacheBundle = cacheBundle;
    }

    @Override
    public String name() {
        return "cache-test";
    }

    @Override
    public void execute(RedgreenRequest request, RedgreenResponse response) {
        try {
            cacheBundle.execute(request, response);
        } catch (RuntimeException e) {
            dataBundle.execute(request, new CacheLineResponse(request, cacheBundle));
            cacheBundle.execute(request, response);
        }
    }

}
