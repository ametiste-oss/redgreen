package org.ametiste.redgreen.configuration;

import com.netflix.config.ConfigurationManager;
import org.ametiste.redgreen.application.FailoverLine;
import org.ametiste.redgreen.application.HystrixSimpleFailoverLine;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class HystrixSimpleFailoverLineConfiguration {

    private static final String COMMAND_EXEC_TIMEOUT_PROPERTY =
            "hystrix.command." +
                HystrixSimpleFailoverLine.HYSTRIX_COMMAND_KEY  +
            ".execution.isolation.thread.timeoutInMilliseconds";

    @Autowired
    private RedgreenBundleRepostitory bundleRepostitory;

    // TODO: extract to boot properties class
    @Value("${redgreen.hystrix.simpleFailoverLine.commandTimeout:300}")
    private String commandExecutionTimeout;

    @Bean
    public HystrixSimpleFailoverLine simpleFailoverLine() {

        // NOTE: there are no another way to set values obtained from properties,
        // hystrix-javanica does not support spring's properties placeholders atm
        ConfigurationManager
                .getConfigInstance()
                .setProperty(COMMAND_EXEC_TIMEOUT_PROPERTY, commandExecutionTimeout);

        return new HystrixSimpleFailoverLine(bundleRepostitory);
    }

}
