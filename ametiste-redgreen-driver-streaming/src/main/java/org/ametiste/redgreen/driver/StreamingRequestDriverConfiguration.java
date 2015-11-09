package org.ametiste.redgreen.driver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
public class StreamingRequestDriverConfiguration {

    /**
     * <p>
     * Bean that used by the {@code spring-webmvc} infrastructure to convert
     * internal {@link ResponseBodyStream} objects to actual client responses.
     * </p>
     *
     * @return 0.1.1
     */
    @Bean
    public ResponseBodyStreamMessageConverter inputStreamMessageConverter() {
        return new ResponseBodyStreamMessageConverter();
    }

    // NOTE: redgreen core registering drivers using spring bean names
    // NOTE: method name is overrided to provide ability to use constant name
    // NOTE: via configuration
    @Bean(name=StreamingRequestDriverFactory.DRIVER_FACTORY_NAME)
    public StreamingRequestDriverFactory simpleStreamDriver() {
        return new StreamingRequestDriverFactory();
    }

}
