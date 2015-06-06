package org.ametiste.redgreen.application.request;

import org.ametiste.redgreen.data.RedgreenRequest;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *
 * @since
 */
public class ResourceRequest {

    public static class Options {

        public final String method;
        public final int cTimeout;
        public final int rTimeout;

        public Options(String method, int cTimeout, int rTimeout) {
            this.method = method;
            this.cTimeout = cTimeout;
            this.rTimeout = rTimeout;
        }
    }

    private final RedgreenRequest rgRequest;

    private final String toResurce;
    private final int connectionTimeout;
    private final int readTimeout;

    public ResourceRequest(RedgreenRequest rgRequest, String toResurce, int connectionTimeout, int readTimeout) {
        this.rgRequest = rgRequest;
        this.toResurce = toResurce;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    public <T> T connectResource(Function<String, T> connectionDetails, BiConsumer<T, Options> connectionSetup) {
        final T c = connectionDetails.apply(toResurce + "?" + rgRequest.requestQuery());
        connectionSetup.accept(c, new Options(rgRequest.requestMethod(), connectionTimeout, readTimeout));
        return c;
    }

}
