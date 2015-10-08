package org.ametiste.redgreen.application.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>
 *      Composite {@link ExecutionLine} factory, this factory aggregates
 *      instances of {@link ExecutionLineFactory} and allow to create required line
 *      of the given type at runtime.
 * </p>
 *
 * @since 0.1.1
 */
// TODO: looks like copipaste of AbstractRequestDriverFactory, may be I need just one abstract factory of two methods?
public class AbstractFailoverLineFactory {

    private final Map<String, ExecutionLineFactory> failoverLineFactories;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractFailoverLineFactory(Map<String, ExecutionLineFactory> failoverLineFactories) {
        // TODO: null & empty checks
        this.failoverLineFactories = failoverLineFactories;
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

}
