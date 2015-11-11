package org.ametiste.redgreen.bundle;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;

/**
 *
 * @since
 */
public interface Bundle {

    String name();

    void execute(RedgreenRequest request, RedgreenResponse response);
}
