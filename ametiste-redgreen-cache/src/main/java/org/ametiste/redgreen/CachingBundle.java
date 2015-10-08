package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.line.ExecutionLine;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.Bundle;

/**
 *
 * @since
 */
public class CachingBundle implements Bundle {

    private final ExecutionLine dataLine;

    private final ExecutionLine cacheLine;

    public CachingBundle(ExecutionLine dataLine, ExecutionLine cacheLine) {
        this.dataLine = dataLine;
        this.cacheLine = cacheLine;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public void execute(RedgreenRequest request, RedgreenResponse response) {

        cacheLine.performRequest(request, null, null, response);



    }

}
