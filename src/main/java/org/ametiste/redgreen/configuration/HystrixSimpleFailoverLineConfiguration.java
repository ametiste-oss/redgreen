package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.FailoverLine;
import org.ametiste.redgreen.application.HystrixSimpleFailoverLine;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     Installs {@link HystrixSimpleFailoverLine} instance into
 *     application context, this instance will use {@link RedgreenBundleRepostitory}
 *     registered within the application context.
 * </p>
 *
 * <p>
 *     Note, in the version 0.1.0 it's only one possible {@link FailoverLine}
 *     configuration.
 * </p>
 *
 * <p>
 *     See {@link HystrixSimpleFailoverLine} documentaion for configuration properties details.
 * </p>
 *
 * @see  HystrixSimpleFailoverLine
 * @since 0.1.0
 */
@Configuration
public class HystrixSimpleFailoverLineConfiguration {

    @Autowired
    private RedgreenBundleRepostitory bundleRepostitory;

    @Bean
    public HystrixSimpleFailoverLine simpleFailoverLine() {
        return new HystrixSimpleFailoverLine(bundleRepostitory);
    }

}
