package org.ametiste.redgreen.application.response;

import java.util.List;
import java.util.Map;

/**
 *
 * @since 0.4.0
 */
public interface RedgreenResponse {

    void sendRedirect(String redirect);

    void attachHeaders(Map<String, List<String>> headers);

    void attachBody(Object builder);

    /**
     * <p>
     *      Finalizing method, must be called after response rendering done,
     *      or if any error accured during response processing.
     * </p>
     *
     * <p>
     *      Primary designed to perform cleanup behaviour that can't be executed
     *      internally or depends on external trigers.
     * </p>
     *
     * <p>
     *      For example, streams could not be closed internally in case of client errors,
     *      so a stream client should close stream.
     * </p>
     *
     * @since 0.4.0
     */
    void purge();

}
