package org.ametiste.redgreen.application.response;

import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public interface RedgreenResponse {

    void forward(Map<String, List<String>> headers, ForwardedResponse body);

}
