package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.line.AbstractFailoverLineFactory;
import org.ametiste.redgreen.application.line.FailoverLineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @since
 */
@Configuration
public class AbstractFailoverLineFactoryConfiguration {

    @Autowired
    private Map<String, FailoverLineFactory> failoverLineFactories;


    @Bean
    public AbstractFailoverLineFactory abstractFailoverLineFactory() {
        return new AbstractFailoverLineFactory(failoverLineFactories);
    }

}
