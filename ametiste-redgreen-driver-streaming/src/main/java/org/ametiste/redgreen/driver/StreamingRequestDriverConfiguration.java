package org.ametiste.redgreen.driver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
public class StreamingRequestDriverConfiguration {

    // NOTE: redgreen core registering drivers using spring bean names
    // NOTE: method name is overrided to provide ability to use constant name
    // NOTE: via configuration
    @Bean(name=StreamingRequestDriverFactory.DRIVER_FACTORY_NAME)
    public StreamingRequestDriverFactory simpleStreamDriver() {
        return new StreamingRequestDriverFactory();
    }

}
