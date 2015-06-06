package org.ametiste.redgreen.application.line.hystrix;

import org.ametiste.redgreen.application.line.FailoverLineFactory;

/**
 * <p>
 *   Note, this factory desinged to don't create failover line, because {@link HystrixSimpleFailoverLine}
 *   is based on {@code spring-aop}, this factory receives configured instance at the initialization stage
 *   and then just provide this instance to its clients.
 * </p>
 *
 * @since 0.1.1
 */
public class HystrixSimpleFailoverLineFactory implements FailoverLineFactory {

    private final HystrixSimpleFailoverLine configuredInstance;

    public HystrixSimpleFailoverLineFactory(HystrixSimpleFailoverLine configuredInstance) {
        this.configuredInstance = configuredInstance;
    }

    @Override
    public HystrixSimpleFailoverLine createFailoverLine() {
        return configuredInstance;
    }

}
