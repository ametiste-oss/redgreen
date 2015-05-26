package org.ametiste.redgreen.application;

import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenRequest;

import java.util.function.Supplier;

/**
 * <p>
 *  Simple protocol that provide operations to execute a {@link RedgreenRequest}
 *  through {@link RedgreenBundle}.
 * </p>
 *
 * <p>
 *  Implementations <b>must</b> provide reliable behaviour where a given request
 *  handled by the "all-or-nothing" scheme.
 * </p>
 *
 * @since 0.1.0
 */
public interface FailoverLine {

    /**
     * <p>
     *     Performs the request provided by a supplier using resources from the named bundle.
     * </p>
     *
     * @param bundleName name of a bundle, which resources would be used to perofm a request
     * @param requestSupplier object to provide {@link RedgreenRequest}
     *
     * @return response, that given by an any successfuly resource
     *
     */
    Object performRequest(String bundleName, Supplier<RedgreenRequest> requestSupplier);

}
