package org.ametiste.redgreen.driver;

import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.ResourceRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;

/**
 *
 * @since
 */
public class DummyDriver implements RequestDriver {
    @Override
    public void executeRequest(ResourceRequest rgRequest, RedgreenResponse redgreenResponse) {
        throw new RuntimeException("Man, I'm dummy :D.");
    }
}
