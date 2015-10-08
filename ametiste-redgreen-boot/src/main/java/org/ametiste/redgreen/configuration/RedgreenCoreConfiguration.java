package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.BaseBundleExecutionService;
import org.ametiste.redgreen.application.BundleExecutionService;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.ametiste.redgreen.interfaces.ForwardedResponseMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     Configures core of the {@code redgreen} application.
 * </p>
 *
 * @since 0.1.1
 */
@Configuration
public class RedgreenCoreConfiguration {

    @Autowired
    private RedgreenBundleRepostitory bundleRepostitory;

    @Bean
    public BundleExecutionService failoverService() {
         return new BaseBundleExecutionService(bundleRepostitory);
    }

    /**
     * <p>
     * Bean that used by the {@code spring-webmvc} infrastructure to convert
     * internal {@link ForwardedResponse} objects to actual client responses.
     * </p>
     *
     * @return 0.1.1
     */
    @Bean
    public ForwardedResponseMessageConverter inputStreamMessageConverter() {
        return new ForwardedResponseMessageConverter();
    }

}
