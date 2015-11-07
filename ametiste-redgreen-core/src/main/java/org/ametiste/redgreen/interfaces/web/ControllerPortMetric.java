package org.ametiste.redgreen.interfaces.web;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since 0.4.0
 */
@MetricsInterface
public interface ControllerPortMetric {

    String FAILOVER_TIMING = "port.controller.failover.timing";

}
