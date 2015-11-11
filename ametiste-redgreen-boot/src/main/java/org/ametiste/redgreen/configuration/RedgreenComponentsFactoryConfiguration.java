package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.RedgreenComponentsFactory;
import org.ametiste.redgreen.application.line.ExecutionLineFactory;
import org.ametiste.redgreen.application.request.RequestDriverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * <p>
 *     Configures set of abstract factorues that used in process of bundles creation.
 * </p>
 *
 * @since 0.1.0
 */
@Configuration
public class RedgreenComponentsFactoryConfiguration {

    @Autowired
    private Map<String, ExecutionLineFactory> failoverLineFactories;

    @Autowired
    private Map<String, RequestDriverFactory> requestDriverFactories;

    @Bean
    public RedgreenComponentsFactory redgreenComponentsFactory() {
        return new RedgreenComponentsFactory(failoverLineFactories, requestDriverFactories);
    }

}
