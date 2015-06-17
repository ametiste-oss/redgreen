package org.ametiste.redgreen.application.line;

import org.ametiste.redgreen.application.bundle.RedgreenPair;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenBundleDescription;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenRequest;

/**
 * <p>
 *  Simple protocol that provide operations to execute a {@link RedgreenRequest}
 *  through {@link RedgreenBundleDescription}.
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
     * @param rgRequest {@link RedgreenRequest} to perform
     * @param rgResponse {@link RedgreenResponse} that would be used to provide response
     *
     * @throws RedgreenBundleDoesNotExist in cases where {@code bundle} targeted by the given
     * {@code RedgreenRequest} can't be found
     *
     */
    void performRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair,
                         RequestDriver requestDriver, RedgreenResponse rgResponse)
            throws RedgreenBundleDoesNotExist;

}
