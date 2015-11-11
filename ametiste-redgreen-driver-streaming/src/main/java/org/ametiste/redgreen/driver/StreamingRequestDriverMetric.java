package org.ametiste.redgreen.driver;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since
 */
@MetricsInterface
public interface StreamingRequestDriverMetric {

    String EXECUTE_TIMING = "driver.streaming-request.execute.timing";

    String GENERAL_ERRORS_COUNT = "driver.streaming-request.execute.general-errors";

}
