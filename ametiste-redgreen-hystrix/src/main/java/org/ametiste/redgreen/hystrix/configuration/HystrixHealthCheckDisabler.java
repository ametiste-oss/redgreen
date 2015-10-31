package org.ametiste.redgreen.hystrix.configuration;

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
 * @since 0.4.0
 */
public class HystrixHealthCheckDisabler implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        final ConfigurableEnvironment environment = event.getEnvironment();
        Properties properties = new Properties();
        properties.put("health.hystrix.enabled", "false");
        environment.getPropertySources().addFirst(
                new PropertiesPropertySource("redgreen-hystrix-properties", properties));
    }

}
