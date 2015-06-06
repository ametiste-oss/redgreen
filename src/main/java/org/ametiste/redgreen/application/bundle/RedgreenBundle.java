package org.ametiste.redgreen.application.bundle;

import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenRequest;

/**
 *
 * @since
 */
public class RedgreenBundle {

    private final RedgreenPair pair;
    private final FailoverLine failoverLine;
    private final RequestDriver requestDriver;

    public RedgreenBundle(RedgreenPair pair, FailoverLine failoverLine, RequestDriver requestDriver) {
        this.pair = pair;
        this.requestDriver = requestDriver;
        this.failoverLine = failoverLine;
    }

    public String name() {
        return pair.getName();
    }

    public <T> T execute(RedgreenRequest request, RedgreenResponse<T> response) {
        return failoverLine.performRequest(request, pair, requestDriver, response);
    }

}
