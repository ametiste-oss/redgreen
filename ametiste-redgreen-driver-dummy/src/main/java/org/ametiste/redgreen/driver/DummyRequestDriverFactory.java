package org.ametiste.redgreen.driver;

import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.RequestDriverFactory;

/**
 *
 * @since
 */
public class DummyRequestDriverFactory implements RequestDriverFactory {

    public static final String DRIVER_FACTORY_NAME = "dummyDriver";

    @Override
    public RequestDriver createRequestDriver() {
        return new DummyDriver();
    }
}
