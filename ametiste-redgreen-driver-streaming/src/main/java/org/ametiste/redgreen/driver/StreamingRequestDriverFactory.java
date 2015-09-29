package org.ametiste.redgreen.driver;

import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.RequestDriverFactory;

/**
 *
 * @since
 */
public class StreamingRequestDriverFactory implements RequestDriverFactory {

    public static final String DRIVER_FACTORY_NAME = "simpleStreamDriver";

    @Override
    public RequestDriver createRequestDriver() {
        return new StreamingRequestDriver();
    }

}
