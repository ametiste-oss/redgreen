package org.ametiste.redgreen.bundle;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.ResourceRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;

import java.net.URL;

/**
 *
 * @since
 */
public class SingleErrorResourceBundle implements Bundle {

    private final String errorResource;
    private final RequestDriver requestDriver;
    private final int connectTimeout;
    private final int readTimeout;

    public SingleErrorResourceBundle(String errorResource, RequestDriver requestDriver, int connectTimeout, int readTimeout) {
        this.errorResource = errorResource;
        this.requestDriver = requestDriver;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    public String name() {
        return "singleErrorResourceBundle";
    }

    @Override
    public void execute(RedgreenRequest request, RedgreenResponse response) {
        requestDriver.executeRequest(
                new ResourceRequest(request, errorResource, connectTimeout, readTimeout),
                response
        );
    }
}
