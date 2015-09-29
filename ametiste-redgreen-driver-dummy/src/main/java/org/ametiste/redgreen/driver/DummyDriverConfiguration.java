package org.ametiste.redgreen.driver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     Dummy configuration of dummy driver.
 * </p>
 *
 * <p>
 *     Mainly presented as example of request driver configuration.
 * </p>
 *
 * @since 0.3.0
 */
@Configuration
public class DummyDriverConfiguration {

    // NOTE: redgreen core registering drivers using spring bean names
    @Bean(name=DummyRequestDriverFactory.DRIVER_FACTORY_NAME)
    public DummyRequestDriverFactory dummyRequestDriver() {
        return new DummyRequestDriverFactory();
    }

}
