package org.ametiste.redgreen.interfaces;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.application.BundleExecutionService;
import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.Bundle;
import org.ametiste.redgreen.data.RedgreenBundleDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This controller accepts requests and handeling its over installed {@link Bundle}.
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
// TODO: add bundles list resource
public class BundleExecutionController {

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
     * {@code BundleExecutionService} that would be used to handle incoming request.
     * </p>
     *
     * @since 0.1.1
     */
    @Autowired
    private BundleExecutionService bundleExecutionService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    @Timeable(name="port.controller.failover.timing")
    public ResponseEntity<ForwardedResponse> performBundleRequest(@PathVariable("bundleName") String bundleName,
                                                                  HttpServletRequest servletRequest) {

        ResponseEntityRedgreenResponse rgResponse = new ResponseEntityRedgreenResponse();

        final RedgreenRequest rgRequest = new RedgreenRequest(
                bundleName,
                servletRequest.getMethod(),
                servletRequest.getQueryString()
        );

        bundleExecutionService.performRequest(rgRequest, rgResponse);

        return rgResponse.takeResponse();

    }

}