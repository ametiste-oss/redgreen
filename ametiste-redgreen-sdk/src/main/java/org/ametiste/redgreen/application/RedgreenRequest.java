package org.ametiste.redgreen.application;

import org.ametiste.redgreen.application.line.FailoverLine;

/**
 * <p>
 *     Value object that represents a {@code request} that should be
 *     performed through concrete resources {@code bundle}.
 * </p>
 *
 * @see RedgreenBundleDescription
 * @see FailoverLine
 * @since 0.1.0
 */
public class RedgreenRequest {

    private final String targetBundle;

    // TODO: I want to use plain string to provide some flexability...
    private final String requestMethod;

    private final String queryString;

    /**
     * <p>
     *     Constructs new {@code request} using provided parameters.
     * </p>
     *
     * <p>
     *     Note, nullable {@code queryString} will be transformed to the empty string.
     * </p>
     *
     * @param targetBundle named of bundle, on which request will be performed, can't be {@code null} or empty
     * @param requestMethod used request method, can't be {@code null} or empty
     * @param queryString provided query string, can be null or empty
     *
     */
    public RedgreenRequest(String targetBundle, String requestMethod, String queryString) {

        if (targetBundle == null || targetBundle.isEmpty()) {
            throw new IllegalArgumentException("Request targetBundle can't be null nor empty.");
        }

        if (requestMethod == null) {
            throw new IllegalArgumentException("Request method can't be null");
        }

        this.targetBundle = targetBundle;
        this.requestMethod = requestMethod;
        this.queryString = queryString == null ? "" : queryString;
    }

    /**
     * <p>
     *   Provides a name of {@code bundle} using which a request should be executed.
     * </p>
     *
     * @return name of target {@code bundle}, can't be {@code null}.
     */
    public String targetBundle() {
        return targetBundle;
    }

    /**
     * <p>
     *  Provides request method used.
     * </p>
     *
     * @return used request method, can't be {@code null}.
     */
    public String requestMethod() {
        return requestMethod;
    }

    /**
     * <p>
     *  Provides query string given with a request.
     * </p>
     *
     * @return provided query string, can't be {@code null}.
     */
    public String requestQuery() {
        return queryString;
    }

}
