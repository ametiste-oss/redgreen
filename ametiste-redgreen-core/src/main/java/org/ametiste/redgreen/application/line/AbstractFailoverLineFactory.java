package org.ametiste.redgreen.application.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>
 *      Composite {@link FailoverLine} factory, this factory aggregates
 *      single instances of {@link FailoverLineFactory} and allow create required instances
 *      types at the runtime.
 * </p>
 *
 * @since 0.1.1
 */
public class AbstractFailoverLineFactory {

    private final Map<String, FailoverLineFactory> failoverLineFactories;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractFailoverLineFactory(Map<String, FailoverLineFactory> failoverLineFactories) {
        // TODO: null & empty checks
        this.failoverLineFactories = failoverLineFactories;
    }

    public FailoverLine createFailoverLine(String lineName) {

        if (!failoverLineFactories.containsKey(lineName)) {

            if (logger.isDebugEnabled()) {
                logger.debug("Available lines is: " + failoverLineFactories);
            }

            throw new IllegalArgumentException("Line with the given name is not registered: " + lineName);
        }

        return failoverLineFactories
                .get(lineName)
                .createFailoverLine();
    }

}
