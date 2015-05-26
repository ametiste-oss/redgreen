package org.ametiste.redgreen.data;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 *
 * @since
 */
public class RedgreenRequest {

    private final HttpMethod httpMethod;

    private final String queryString;

    public RedgreenRequest(HttpMethod httpMethod, String queryString) {
        this.httpMethod = httpMethod;
        this.queryString = queryString;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getQueryString() {
        return queryString;
    }

}
