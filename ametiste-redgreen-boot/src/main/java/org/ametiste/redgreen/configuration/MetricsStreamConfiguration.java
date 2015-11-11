package org.ametiste.redgreen.configuration;

import org.ametiste.metrics.MetricsService;
import org.ametiste.metrics.NullMetricsAggregator;
import org.ametiste.metrics.aop.*;
import org.ametiste.metrics.experimental.streams.AggregatorStream;
import org.ametiste.metrics.experimental.streams.MetaMetricsStream;
import org.ametiste.metrics.experimental.streams.MetricsStream;
import org.ametiste.metrics.experimental.streams.MetricsStreamService;
import org.ametiste.metrics.experimental.streams.configuration.StreamMetricsAggregatorConfiguration;
import org.ametiste.metrics.jmx.JmxMetricAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 *
 * @since
 */
@Configuration
public class MetricsStreamConfiguration {

    @Bean
    public MetricsService metricsService() {
        return new MetricsStreamService(metricsStream());
    }

    @Bean
    public MetricsStream metricsStream() {
        return new MetaMetricsStream(
            new AggregatorStream(new JmxMetricAggregator("org.ametiste.meta")),
            new AggregatorStream(new JmxMetricAggregator("org.ametiste.rg.meta"))
        );
    }

}
