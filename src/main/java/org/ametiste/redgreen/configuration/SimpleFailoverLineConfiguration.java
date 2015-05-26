package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.SimpleFailoverLine;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
public class SimpleFailoverLineConfiguration {

    @Autowired
    private RedgreenBundleRepostitory bundleRepostitory;

    @Bean
    public SimpleFailoverLine simpleFailoverLine() {
        return new SimpleFailoverLine(bundleRepostitory);
    }

}
