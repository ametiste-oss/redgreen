package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.line.AbstractFailoverLineFactory;
import org.ametiste.redgreen.application.BaseFailoverService;
import org.ametiste.redgreen.application.line.FailoverLineFactory;
import org.ametiste.redgreen.application.FailoverService;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
    public FailoverService failoverService() {
         return new BaseFailoverService(bundleRepostitory);
    }

}
