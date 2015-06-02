package org.ametiste.redgreen.interfaces;

import org.ametiste.redgreen.application.FailoverLine;
import org.ametiste.redgreen.application.ForwardedResponse;
import org.ametiste.redgreen.application.Forwarder;
import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *      This controller accepts requests and handeling its over installed {@link FailoverLine}.
 * </p>
 *
 * <p>
 *      Controller serves the {@code bundle} resource that represent a set of resources that
 *      may handle incoming request.
 * </p>
 *
 * <p>
 *     Note, only GET, OPTIONS and HEAD methods are supported by the controller,
 *     method which could change the server state does not supported by a safety reasons.
 * </p>
 *
 * @see RedgreenBundle
 * @see RedgreenRequest
 * @since 0.1.0
 */
@RestController
@RequestMapping("/")
public class FailoverLineController {

    /**
     * <p>
     *     {@code FailoverLine} that would be used to handle incoming request,
     *     note for the version 0.1.0 is only one {@code failover line} could be
     *     used within the entire application.
     * </p>
     *
     * @since 0.1.0
     */
    @Autowired
    private FailoverLine failoverLine;

    /**
     * <p>
     *     Note, only GET, OPTIONS and HEAD methods are supported,
     *     method which could change the server state does not supported by a safety reasons.
     * </p>
     *
     * <p>
     *     Also, this method provides <i>resource</i> failover capabilities, it means that
     *     a <i>green</i> resource would be failovered against <i>red</i> resources in a bundle.
     *     This method can't be used as a proxy for range of resources prefixed by the <i>green</i>
     *     resource path.
     * </p>
     *
     * @param bundleName
     * @return response acquired from a one of bundeled resources
     */
    @RequestMapping(value = "/{bundleName:.+}",
        method = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
    public ResponseEntity<ForwardedResponse> performIncomingRequest(@PathVariable("bundleName") String bundleName,
            HttpServletRequest servletRequest) {

        return failoverLine.performRequest(

                () -> {
                    return new RedgreenRequest(
                            bundleName,
                            HttpMethod.valueOf(servletRequest.getMethod()),
                            servletRequest.getQueryString()
                    );
                },

                new Forwarder<ResponseEntity<ForwardedResponse>>() {
                    public ResponseEntity<ForwardedResponse> forward(Map<String, List<String>> headers, ForwardedResponse body) {
                        return new ResponseEntity<ForwardedResponse>(body,
                                new LinkedMultiValueMap<String, String>(headers), HttpStatus.OK);
                    }
                }

        );

    }

}
