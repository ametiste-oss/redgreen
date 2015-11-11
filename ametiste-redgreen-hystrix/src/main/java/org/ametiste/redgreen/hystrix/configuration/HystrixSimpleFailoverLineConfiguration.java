package org.ametiste.redgreen.hystrix.configuration;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.ametiste.redgreen.application.process.FailoverProcess;
import org.ametiste.redgreen.application.process.simple.SimpleFailoverProcess;
import org.ametiste.redgreen.hystrix.line.HystrixSimpleExecutionLineFactory;
import org.ametiste.redgreen.hystrix.line.HystrixSimpleFailoverLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * <p>
 *     Installs {@link HystrixSimpleFailoverLine} into
 *     application context.
 * </p>
 *
 * <p>
 *     Installed failover line is bound to {@code hystrixSimpleLine} line factory name, this name can
 *     be used in a bundle configurations to assign the bundle execution to this line.
 * </p>
 *
 * <p>
 *     Note, {@code HystrixSimpleFailoverLine} is <b>singletone</b> line, it means that
 *     all bundles which bounded to this line will share same instance to execute requests,
 *     its thread pool and failover symaphore pool will be shared too.
 * </p>
 *
 * <p>
 *     This line may be useful when complex configuration is not required, set of failovered resources
 *     are small, or when a global bandwidth entry point is required.
 * </p>
 *
 * <p>
 *     Command execution timeout can be modified using application
 *     property <i>redgreen.hystrix.hystrixSimpleFailoverLine.commandTimeout</i> ( note, a value should be defined in millis ).
 * </p>
 *
 * <p>
 *     See {@link HystrixSimpleFailoverLine} documentaion for implementation details.
 * </p>
 *
 * @see  HystrixSimpleFailoverLine
 *
 * @since 0.1.0
 */
@Configuration
@EnableHystrixDashboard
@EnableCircuitBreaker
public class HystrixSimpleFailoverLineConfiguration {

    /**
     * <p>
     *     Name of factory on which it will be registered within the application context.
     * </p>
     * <p>
     *     This name can be used in the bundles configuration to specify a bundle execution line.
     * </p>
     *
     * @since 0.1.1
     */
    public static final String LINE_FACTORY_NAME = "hystrixSimpleLine";

    private static final String COMMAND_EXEC_TIMEOUT_PROPERTY = cmdPropertyName(
            "execution.isolation.thread.timeoutInMilliseconds"
    );

    private static final String FALLBACK_MAX_CONCUREENT_REQUESTS_PROPERTY = cmdPropertyName(
            "fallback.isolation.semaphore.maxConcurrentRequests"
    );

    private static final String THREADS_POOL_SIZE_PROPERTY =
            "hystrix.threadpool.HystrixThreadPoolKey.coreSize";

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.hystrixSimpleFailoverLine.commandTimeout:300}")
    private String commandExecutionTimeout;

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.hystrixSimpleFailoverLine.threadPoolSize:4}")
    private int threadPoolSize;

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.hystrixSimpleFailoverLine.failoverPoolSize:4}")
    private int failoverPoolSize;

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
    // TODO: I need the way to customize FailoverProcess creation process, some kind of factory required
    public FailoverProcess hystrixFailoverProcess() {
        return new SimpleFailoverProcess();
    }

    @Bean
    // NOTE: @Scope used to change proxyMode (for proxy that created by hystrix-javanica),
    // but we need class-based proxy to have ability to create factory that operates
    // by concrete classes instances
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HystrixSimpleFailoverLine hystrixSimpleFailoverLine() {

        // NOTE: there are no another way to set values obtained from properties,
        // hystrix-javanica does not support spring's properties placeholders atm

        ConfigurationManager
                .getConfigInstance()
                .setProperty(COMMAND_EXEC_TIMEOUT_PROPERTY, commandExecutionTimeout);

        ConfigurationManager
                .getConfigInstance()
                .setProperty(FALLBACK_MAX_CONCUREENT_REQUESTS_PROPERTY, failoverPoolSize);


        // TODO: I don't want to affect global thread pool, need to find the way how to specify
        // properties for concerete command thread pool
        ConfigurationManager
                .getConfigInstance()
                .setProperty("hystrix.threadpool.default.coreSize", threadPoolSize);

        ConfigurationManager
                .getConfigInstance()
                .setProperty("hystrix.command.default.circuitBreaker.enabled", "false");

        HystrixThreadPoolProperties.Setter()
                .withCoreSize(threadPoolSize);

        return new HystrixSimpleFailoverLine(hystrixFailoverProcess());
    }

    @Bean(name=LINE_FACTORY_NAME)
    public HystrixSimpleExecutionLineFactory hystrixSimpleFailoverLineFactory() {
        return new HystrixSimpleExecutionLineFactory(hystrixSimpleFailoverLine());
    }

    private static final String cmdPropertyName(String propertyName) {
        return "hystrix.command." +
                HystrixSimpleFailoverLine.HYSTRIX_COMMAND_KEY  + "." + propertyName;
    }

}
