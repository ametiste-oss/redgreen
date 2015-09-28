package org.ametiste.redgreen.bundle;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.application.request.RequestDriver;

/**
 * <p>
 *     This class defines {@code resources bundle}, each bundle has unique defined
 *     pair of {@code green/red} resources, bounded failover line to be executed on and
 *     associated request driver to execute requests to the bundled resources.
 * </p>
 *
 * @since 0.1.1
 */
public class RedgreenBundle implements Bundle {

    private final RedgreenPair pair;

    private final FailoverLine failoverLine;

    private final RequestDriver requestDriver;

    public RedgreenBundle(RedgreenPair pair, FailoverLine failoverLine, RequestDriver requestDriver) {
        this.pair = pair;
        this.requestDriver = requestDriver;
        this.failoverLine = failoverLine;
    }

    @Override
    public String name() {
        return pair.getName();
    }

    /**
     * <p>
     *  Executes the given request via bundle's failover line, the given
     *  response object would be used to provide response to the client.
     * </p>
     *
     * @param request request to be executed
     * @param response response object that would be used to provide response to the client
     *
     * @return
     */
    @Override
    public void execute(RedgreenRequest request, RedgreenResponse response) {
        failoverLine.performRequest(request, pair, requestDriver, response);
    }

}
