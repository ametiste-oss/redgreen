package org.ametiste.redgreen.interfaces;

import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.application.FailoverService;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.data.RedgreenBundleDescription;
import org.ametiste.redgreen.application.RedgreenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This controller accepts requests and handeling its over installed {@link FailoverLine}.
 * </p>
 * <p>
 * <p>
 * Controller serves the {@code bundle} resource that represent a set of resources that
 * may handle incoming request.
 * </p>
 * <p>
 * <p>
 * Note, only GET, OPTIONS and HEAD methods are supported by the controller,
 * method which could change the server state does not supported by a safety reasons.
 * </p>
 *
 * @see RedgreenBundleDescription
 * @see RedgreenRequest
 * @since 0.1.0
 */
@RestController
@RequestMapping("/")
public class FailoverLineController {

    private static class ResponseEntityRedgreenResponse implements RedgreenResponse {

        private ResponseEntity<ForwardedResponse> responseEntity;

        public ResponseEntity<ForwardedResponse> takeResponse() {

            if (responseEntity == null) {
                throw new IllegalStateException("RedgreenResponse was not forwarded.");
            }

            return responseEntity;
        }

        public void forward(Map<String, List<String>> headers, ForwardedResponse body) {

            if (responseEntity != null) {
                throw new IllegalStateException("RedgreenResponse already forwarded.");
            }

            responseEntity = new ResponseEntity<ForwardedResponse>(body,
                    new LinkedMultiValueMap<String, String>(headers), HttpStatus.OK);

        }
    }

    /**
     * <p>
     * {@code FailoverService} that would be used to handle incoming request.
     * </p>
     *
     * @since 0.1.1
     */
    @Autowired
    private FailoverService failoverService;

    /**
     * <p>
     * Note, only GET, OPTIONS and HEAD methods are supported,
     * method which could change the server state does not supported by a safety reasons.
     * </p>
     * <p>
     * <p>
     * Also, this method provides <i>resource</i> failover capabilities, it means that
     * a <i>green</i> resource would be failovered against <i>red</i> resources in a bundle.
     * This method can't be used as a proxy for range of resources prefixed by the <i>green</i>
     * resource path.
     * </p>
     *
     * @param bundleName
     * @return response acquired from a one of bundeled resources
     */
    @RequestMapping(value = "/{bundleName:.+}",
            method = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
    public ResponseEntity<ForwardedResponse> performIncomingRequest(@PathVariable("bundleName") String bundleName,
                                                                    HttpServletRequest servletRequest) {

        ResponseEntityRedgreenResponse rgResponse = new ResponseEntityRedgreenResponse();

        final RedgreenRequest rgRequest = new RedgreenRequest(
                bundleName,
                servletRequest.getMethod(),
                servletRequest.getQueryString()
        );

        failoverService.performRequest(rgRequest, rgResponse);

        return rgResponse.takeResponse();

    }

}