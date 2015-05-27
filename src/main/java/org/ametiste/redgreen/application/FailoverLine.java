package org.ametiste.redgreen.application;

import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
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
     * @param requestSupplier {@link RedgreenRequest} supplier object
     *
     * @return response, that given by an any successfuly resource
     *
     * @throws RedgreenBundleDoesNotExist in cases where {@code bundle} targeted by the given
     * {@code RedgreenRequest} can't be found
     *
     */
    Object performRequest(Supplier<RedgreenRequest> requestSupplier)
            throws RedgreenBundleDoesNotExist;

}
