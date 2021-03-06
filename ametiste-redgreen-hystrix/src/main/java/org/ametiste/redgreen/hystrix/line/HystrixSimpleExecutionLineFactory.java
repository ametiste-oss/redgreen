package org.ametiste.redgreen.hystrix.line;

import org.ametiste.redgreen.application.line.ExecutionLineFactory;

/**
 * <p>
 *   Note, this factory desinged to don't create failover line, because {@link HystrixSimpleFailoverLine}
 *   is based on {@code spring-aop}, this factory receives configured instance at the initialization stage
 *   and then just provide this instance to its clients.
 * </p>
 *
 * @since 0.1.1
 */
public class HystrixSimpleExecutionLineFactory implements ExecutionLineFactory {

    private final HystrixSimpleFailoverLine configuredInstance;

    public HystrixSimpleExecutionLineFactory(HystrixSimpleFailoverLine configuredInstance) {
        this.configuredInstance = configuredInstance;
    }

    @Override
    public HystrixSimpleFailoverLine createComponent() {
        return configuredInstance;
    }

}
