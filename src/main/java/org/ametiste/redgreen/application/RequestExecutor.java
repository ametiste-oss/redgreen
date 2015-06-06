package org.ametiste.redgreen.application;

/**
 *
 * @since
 */
public interface RequestExecutor {

    <T> T executeRequest(String method, String url, Forwarder<T> forwarder);

}
