package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.line.AbstractFailoverLineFactory;
import org.ametiste.redgreen.application.line.FailoverLineFactory;
import org.ametiste.redgreen.application.request.RequestDriverFactory;
import org.ametiste.redgreen.request.AbstractRequestDriverFactory;
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
public class AbstractFactoriesConfiguration {

    @Autowired
    private Map<String, FailoverLineFactory> failoverLineFactories;

    @Autowired
    private Map<String, RequestDriverFactory> requestDriverFactories;

    @Bean
    public AbstractFailoverLineFactory abstractFailoverLineFactory() {
        return new AbstractFailoverLineFactory(failoverLineFactories);
    }

    @Bean
    public AbstractRequestDriverFactory abstractRequestDriverFactory() {
        return new AbstractRequestDriverFactory(requestDriverFactories);
    }

}
