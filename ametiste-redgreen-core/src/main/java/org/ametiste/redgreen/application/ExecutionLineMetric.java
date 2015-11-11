package org.ametiste.redgreen.application;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since 0.4.0
 */
@MetricsInterface
public interface ExecutionLineMetric {

    String PERFORM_TIMING = "execuiton.line.base.perform.timing";

    String FAILOVER_GENERAL_ERRORS = "execuiton.line.base.perform.general-errors";

}
