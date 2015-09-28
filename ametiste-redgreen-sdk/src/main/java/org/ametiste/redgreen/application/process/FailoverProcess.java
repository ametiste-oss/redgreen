package org.ametiste.redgreen.application.process;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.RedgreenPair;

/**
 *
 * @since 0.3.0
 */
public interface FailoverProcess {

    void performMain(RedgreenRequest rgRequest,
                     RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse);

    void performFallback(RedgreenRequest rgRequest,
                         RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse);

}
