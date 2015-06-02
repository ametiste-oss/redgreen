package org.ametiste.redgreen;

import org.ametiste.redgreen.configuration.DirectRedgreenBundleRepositoryConfiguration;
import org.ametiste.redgreen.configuration.HystrixSimpleFailoverLineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;

/**
 * <p>
 *     Redgreen application main class. Includes {@code Hystrix Circuit Breaker}
 *     and {@code Hystrix Dashboard} modules.
 * </p>
 *
 * <p>
 *    {@code Hystrix Dashboard} is available by URI <i>http://127.0.0.1:8080/hystrix</i>,
 *    {@code Hystrix Stream} is available by <i>http://127.0.0.1:8080/hystrix.stream</i>.
 * </p>
 *
 * <p>
 *     Note, see a configuration classes for details about application configuration processing.
 * </p>
 *
 * @see HystrixSimpleFailoverLineConfiguration
 * @see DirectRedgreenBundleRepositoryConfiguration
 * @since 0.1.0
 */
@SpringBootApplication
public class RedgreenApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RedgreenApplication.class, args);
    }

}
