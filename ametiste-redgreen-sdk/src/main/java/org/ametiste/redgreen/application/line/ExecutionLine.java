package org.ametiste.redgreen.application.line;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.RedgreenPair;
import org.ametiste.redgreen.application.request.RequestDriver;

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
// TODO : may be rename to ExecutionLine? Object that defines how execution going?
public interface ExecutionLine {

    /**
     * <p>
     *     Performs the request provided by a supplier using resources from the named bundle.
     * </p>
     *
     * @param rgRequest {@link RedgreenRequest} to perform
     * @param rgResponse {@link RedgreenResponse} that would be used to provide response
     *
     * @throws ExecutionLineException in cases of underlaying errors through line implementation,
     * also should be thrown line failed or can't get result of execution.
     *
     */
    void performRequest(RedgreenRequest rgRequest, RedgreenPair resourcesPair,
                         RequestDriver requestDriver, RedgreenResponse rgResponse) throws ExecutionLineException;

}
