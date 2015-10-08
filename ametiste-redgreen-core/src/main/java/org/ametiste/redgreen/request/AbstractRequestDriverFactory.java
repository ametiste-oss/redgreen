package org.ametiste.redgreen.request;

import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.RequestDriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>
 *      Composite {@link RequestDriver} factory, this factory aggregates
 *      instances of {@link RequestDriverFactory} and allow to create required driver
 *      of the given type at runtime.
 * </p>
 *
 * @since 0.3.0
 */
public class AbstractRequestDriverFactory {

    private final Map<String, RequestDriverFactory> driverFactories;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractRequestDriverFactory(Map<String, RequestDriverFactory> driverFactories) {
        // TODO: null & empty checks
        this.driverFactories = driverFactories;
    }

    public RequestDriver createRequestDriver(String driverName) {

        if (!driverFactories.containsKey(driverName)) {

            if (logger.isDebugEnabled()) {
                logger.debug("Available drivers are: " + driverFactories);
            }

            throw new IllegalArgumentException("Driver with the given name is not registered: " + driverName);
        }

        return driverFactories
                .get(driverName)
                .createRequestDriver();
    }


}
