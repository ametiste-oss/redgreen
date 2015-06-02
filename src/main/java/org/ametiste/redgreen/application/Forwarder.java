package org.ametiste.redgreen.application;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public interface Forwarder<T> {

    T forward(Map<String, List<String>> headers, ForwardedResponse body);

}
