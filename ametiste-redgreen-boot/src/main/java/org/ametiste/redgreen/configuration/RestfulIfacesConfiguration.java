package org.ametiste.redgreen.configuration;

import org.ametiste.ifaces.api.error.ApiDirectErrorMappingStrategy;
import org.ametiste.ifaces.api.error.http.SimpleStatusMapper;
import org.ametiste.ifaces.api.error.http.mappers.BadRequestExceptionGroupMapper;
import org.ametiste.ifaces.api.error.http.mappers.InternalErrorExceptionGroupMapper;
import org.ametiste.ifaces.api.error.http.mappers.ResourceNotFoundExceptionGroupMapper;
import org.ametiste.ifaces.api.error.http.mappers.ServiceUnavailableExceptionGroupMapper;
import org.ametiste.ifaces.restful.AMERequestIdAppender;
import org.ametiste.ifaces.restful.ApiControllerMappingAdvice;
import org.ametiste.ifaces.restful.EventExceptionResolver;
import org.ametiste.ifaces.restful.RequestIdHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 *
 * @since
 */
@Configuration
public class RestfulIfacesConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestIdHolder ameRequestIdHolder() {
        return new RequestIdHolder();
    }

    @Bean
    public AMERequestIdAppender ameRequestIdAppender() {
        final AMERequestIdAppender ameRequestIdAppender = new AMERequestIdAppender();
        ameRequestIdAppender.setRequestIdHolder(ameRequestIdHolder());
        return ameRequestIdAppender;
    }

    @Bean
    public EventExceptionResolver eventExceptionResolver() {
        final EventExceptionResolver eventExceptionResolver = new EventExceptionResolver();
        eventExceptionResolver.setRequestIdHolder(ameRequestIdHolder());
        return eventExceptionResolver;
    }

    @Bean
    public ApiControllerMappingAdvice apiControllerMappingAdvice() {
        return new ApiControllerMappingAdvice();
    }

    @Bean
    @Qualifier("errorMappingStrategy")
    public ApiDirectErrorMappingStrategy apiDirectErrorMappingStrategy() {

        final ApiDirectErrorMappingStrategy directErrorMappingStrategy = new ApiDirectErrorMappingStrategy();

        directErrorMappingStrategy.setMappers(
                Arrays.asList(
                    new BadRequestExceptionGroupMapper(),
                    new ResourceNotFoundExceptionGroupMapper(),
                    new ServiceUnavailableExceptionGroupMapper(),
                    new InternalErrorExceptionGroupMapper()
                )
        );

        return directErrorMappingStrategy;
    }

    @Bean
    @Qualifier("statusMappingStrategy")
    public SimpleStatusMapper simpleStatusMapper() {
        return new SimpleStatusMapper();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ameRequestIdAppender());
    }
}
