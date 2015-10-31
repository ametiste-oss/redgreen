package org.ametiste.redgreen.configuration;

import org.ametiste.metrics.MetricsService;
import org.ametiste.metrics.NullMetricsAggregator;
import org.ametiste.metrics.aop.*;
import org.ametiste.metrics.boot.configuration.MetricsCoreConfiguration;
import org.ametiste.metrics.experimental.streams.AggregatorStream;
import org.ametiste.metrics.experimental.streams.MetaMetricsStream;
import org.ametiste.metrics.experimental.streams.MetricsStreamService;
import org.ametiste.metrics.jmx.JmxMetricAggregator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 *
 * @since
 */
@Configuration
public class MetricsStreamConfiguration {

    @Bean
    public TimeableAspect timeableAspect() {
        return new TimeableAspect(metricsService(), nameResolver());
    }

    @Bean
    public CountableAspect countableAspect() {
        return new CountableAspect(metricsService(), nameResolver());
    }

    @Bean
    public ErrorCountableAspect errorCountableAspect() {
        return new ErrorCountableAspect(metricsService(), nameResolver());
    }

    @Bean
    public ChronableAspect chronableAspect() {
        return new ChronableAspect(metricsService(), nameResolver(), spelParser());
    }

    @Bean
    public SpelExpressionParser spelParser() {
        return new SpelExpressionParser();
    }

    @Bean
    public IdentifierResolver nameResolver() {
        return new IdentifierResolver(spelParser());
    }

    @Bean
    public MetricsService metricsService() {
        return new MetricsStreamService(
                new MetaMetricsStream(
                        new AggregatorStream(new NullMetricsAggregator()),
                        new AggregatorStream(new JmxMetricAggregator("org.ametiste.rg"))
                )
        );
    }

}
