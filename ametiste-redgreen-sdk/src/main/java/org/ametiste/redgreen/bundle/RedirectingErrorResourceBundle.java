package org.ametiste.redgreen.bundle;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;

/**
 *
 * @since
 */
public class RedirectingErrorResourceBundle implements Bundle {

    private final String errorResource;

    public RedirectingErrorResourceBundle(String errorResource) {
        this.errorResource = errorResource;
    }

    @Override
    public String name() {
        return "redirectingErrorResourceBundle";
    }

    @Override
    public void execute(RedgreenRequest request, RedgreenResponse response) {
        response.sendRedirect(errorResource);
    }
}
