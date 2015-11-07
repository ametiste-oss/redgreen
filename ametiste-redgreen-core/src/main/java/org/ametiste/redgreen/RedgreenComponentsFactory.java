package org.ametiste.redgreen;

import org.ametiste.redgreen.application.line.ExecutionLine;
import org.ametiste.redgreen.application.line.ExecutionLineFactory;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.RequestDriverFactory;
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

    public ExecutionLine createFailoverLine(String driverName) {

        if (!failoverLineFactories.containsKey(driverName)) {

            if (logger.isDebugEnabled()) {
                logger.debug("Available lines are: " + failoverLineFactories);
            }

            throw new IllegalArgumentException("Line with the given name is not registered: " + driverName);
        }

        return failoverLineFactories
                .get(driverName)
                .createLine();
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
