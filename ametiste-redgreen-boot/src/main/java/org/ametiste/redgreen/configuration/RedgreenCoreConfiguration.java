package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.application.BaseBundleExecutionService;
import org.ametiste.redgreen.application.BundleExecutionService;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

/**
 * <p>
 *     Configures core of the {@code redgreen} application.
 * </p>
 *
 * @since 0.1.1
 */
@Configuration
public class RedgreenCoreConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private RedgreenBundleRepostitory bundleRepostitory;

    @Bean
    public BundleExecutionService failoverService() {
         return new BaseBundleExecutionService(bundleRepostitory);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public ViewResolver jsonViewResolver() {
        return new ViewResolver() {
            @Override
            public View resolveViewName(String viewName, Locale locale) throws Exception {
                return new MappingJackson2JsonView();
            }
        };
    }

}
