package org.ametiste.redgreen.configuration;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.ametiste.redgreen.application.FailoverLine;
import org.ametiste.redgreen.application.HystrixSimpleFailoverLine;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.ametiste.redgreen.interfaces.ForwardedResponseMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     Installs {@link HystrixSimpleFailoverLine} instance into
 *     application context, this instance will use {@link RedgreenBundleRepostitory}
 *     registered within the application context.
 * </p>
 *
 * <p>
 *     Command execution timeout can be modified using application
 *     property <i>redgreen.hystrix.simpleFailoverLine.commandTimeout</i> ( note, a value should be defined in millis ).
 * </p>
 *
 * <p>
 *     See {@link HystrixSimpleFailoverLine} documentaion for implementation details.
 * </p>
 *
 * <p>
 *     Note, in the version 0.1.0 it's only one possible {@link FailoverLine}
 *     configuration.
 * </p>
 *
 * @see  HystrixSimpleFailoverLine
 * @since 0.1.0
 */
@Configuration
@EnableHystrixDashboard
@EnableCircuitBreaker
public class HystrixSimpleFailoverLineConfiguration {

    private static final String COMMAND_EXEC_TIMEOUT_PROPERTY = cmdPropertyName(
            "execution.isolation.thread.timeoutInMilliseconds"
    );

    private static final String FALLBACK_MAX_CONCUREENT_REQUESTS_PROPERTY = cmdPropertyName(
            "fallback.isolation.semaphore.maxConcurrentRequests"
    );

    private static final String THREADS_POOL_SIZE_PROPERTY =
            "hystrix.threadpool.HystrixThreadPoolKey.coreSize";

    @Autowired
    private RedgreenBundleRepostitory bundleRepostitory;

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.simpleFailoverLine.commandTimeout:300}")
    private String commandExecutionTimeout;

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.simpleFailoverLine.threadPoolSize:4}")
    private int threadPoolSize;

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.simpleFailoverLine.failoverPoolSize:4}")
    private int failoverPoolSize;

    @Bean
    public HystrixSimpleFailoverLine simpleFailoverLine() {

        // NOTE: there are no another way to set values obtained from properties,
        // hystrix-javanica does not support spring's properties placeholders atm

        ConfigurationManager
                .getConfigInstance()
                .setProperty(COMMAND_EXEC_TIMEOUT_PROPERTY, commandExecutionTimeout);

        ConfigurationManager
                .getConfigInstance()
                .setProperty(FALLBACK_MAX_CONCUREENT_REQUESTS_PROPERTY, failoverPoolSize);

        ConfigurationManager
                .getConfigInstance()
                .setProperty("hystrix.threadpool.default.coreSize", threadPoolSize);

        ConfigurationManager
                .getConfigInstance()
                .setProperty("hystrix.command.default.circuitBreaker.enabled", "false");

        HystrixThreadPoolProperties.Setter()
                .withCoreSize(threadPoolSize);

        return new HystrixSimpleFailoverLine(bundleRepostitory);
    }

    @Bean
    public ForwardedResponseMessageConverter inputStreamMessageConverter() {
        return new ForwardedResponseMessageConverter();
    }

    private static final String cmdPropertyName(String propertyName) {
        return "hystrix.command." +
                HystrixSimpleFailoverLine.HYSTRIX_COMMAND_KEY  + "." + propertyName;
    }

}
