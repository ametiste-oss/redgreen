package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public class InMemoryCacheBundle implements CacheBundle {

    private final Map<String, String> cache = new HashMap<>();

    @Override
    public String name() {
        return null;
    }

    @Override
    public void execute(RedgreenRequest request, RedgreenResponse response) {

        if (!cache.containsKey(request.requestQuery())) {
             throw new RuntimeException("Can't find cache for: " + request.requestQuery());
        }

        // response.attachBody();

    }

//    @Override
//    public void cacheResponse(RedgreenRequest request, ResponseBodyStream responseBodyStream) {
//
//        try ( ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
//            responseBodyStream.writeBody(arrayOutputStream);
//            cache.put(request.requestQuery(), new String(arrayOutputStream.toByteArray()));
//        } catch (IOException e) {
//            /// NOTHING
//        }
//
//    }
}
