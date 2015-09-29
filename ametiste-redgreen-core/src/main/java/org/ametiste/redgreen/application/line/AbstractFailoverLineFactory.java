package org.ametiste.redgreen.application.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>
 *      Composite {@link FailoverLine} factory, this factory aggregates
 *      instances of {@link FailoverLineFactory} and allow to create required line
 *      of the given type at runtime.
 * </p>
 *
 * @since 0.1.1
 */
// TODO: looks like copipaste of AbstractRequestDriverFactory, may be I need just one abstract factory of two methods?
public class AbstractFailoverLineFactory {

    private final Map<String, FailoverLineFactory> failoverLineFactories;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractFailoverLineFactory(Map<String, FailoverLineFactory> failoverLineFactories) {
        // TODO: null & empty checks
        this.failoverLineFactories = failoverLineFactories;
    }

    public FailoverLine createFailoverLine(String driverName) {

        if (!failoverLineFactories.containsKey(driverName)) {

            if (logger.isDebugEnabled()) {
                logger.debug("Available lines are: " + failoverLineFactories);
            }

            throw new IllegalArgumentException("Line with the given name is not registered: " + driverName);
        }

        return failoverLineFactories
                .get(driverName)
                .createFailoverLine();
    }

}
