package org.ametiste.redgreen;

import org.ametiste.redgreen.application.line.ExecutionLine;
import org.ametiste.redgreen.application.line.ExecutionLineFactory;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.RequestDriverFactory;
import org.ametiste.redgreen.component.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>
 *      Redgreen components factory, used during configuration and at runtime,
 *      aggregates instances of {@link ExecutionLineFactory}, {@link RequestDriverFactory}
 *      and allow to create required line and drivers of the given types.
 * </p>
 *
 * @since 0.4.0
 */
public class RedgreenComponentsFactory {

    private final Map<String, RequestDriverFactory> driverFactories;
    private final Map<String, ExecutionLineFactory> failoverLineFactories;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public RedgreenComponentsFactory(Map<String, ExecutionLineFactory> failoverLineFactories,
                                     Map<String, RequestDriverFactory> driverFactories) {
        this.failoverLineFactories = failoverLineFactories;
        this.driverFactories = driverFactories;
    }

    public ExecutionLine createFailoverLine(String lineName) {
        return createComponent(lineName, failoverLineFactories, ExecutionLine.class);
    }

    public RequestDriver createRequestDriver(String driverName) {
        return createComponent(driverName, driverFactories, RequestDriver.class);
    }

    private <T> T createComponent(String componentName,
                                  Map<String, ? extends ComponentFactory<T>> factories,
                                  Class<? extends  T> componentClass) {

        if (!factories.containsKey(componentName)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Available components " + componentClass + " are: " + factories);
            }

            throw new IllegalArgumentException("Component "
                    + componentClass +  " with the given name is not registered: " + componentName);
        }

        return factories
                .get(componentName)
                .createComponent();
    }

}
