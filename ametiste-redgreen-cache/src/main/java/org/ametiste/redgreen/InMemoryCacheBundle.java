package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.application.response.RedgreenResponse;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

        response.forward(
                Collections.emptyMap(),
                new ForwardedResponse() {
                    @Override
                    public void forwardTo(OutputStream outputStream) {
                        try {
                            outputStream.write(cache.get(request.requestQuery()).getBytes());
                        } catch (IOException e) {

                        }
                    }

                    @Override
                    public void close() throws IOException {

                    }
                }
        );

    }

    @Override
    public void cacheResponse(RedgreenRequest request, ForwardedResponse forwardedResponse) {

        try ( ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            forwardedResponse.forwardTo(arrayOutputStream);
            cache.put(request.requestQuery(), new String(arrayOutputStream.toByteArray()));
        } catch (IOException e) {
            /// NOTHING
        }

    }
}
