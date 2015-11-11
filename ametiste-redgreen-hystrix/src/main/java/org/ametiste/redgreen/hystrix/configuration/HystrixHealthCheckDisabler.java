package org.ametiste.redgreen.hystrix.configuration;

import org.ametiste.bootex.progprops.ApplicationPropertiesConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * <p>
 *     <B>Workaround</B> fix of spring-cloud-netflix problem, that not allow
 *     to check health of hystrix when commands without circuit breaker are confiugred.
 * </p>
 *
 * <p>
 *     Disabled health check of hystrix integration. Since there is no checks for something else
 *     of opened circuits, this check is not applicable within the current redgreen implementation.
 * </p>
 *
 * <p>
 *     Note, since it is application startup listener, META-INF/spring.factories is used to
 *     provide link to this configuration to application bootstrap.
 * </p>
 *
 * @since 0.4.0
 */
public class HystrixHealthCheckDisabler extends ApplicationPropertiesConfiguration {

    @Override
    protected void configureProperties() {
        defProperty("health.hystrix.enabled", "false");
    }

}
