package org.ametiste.redgreen.interfaces;

import org.ametiste.redgreen.application.FailoverLine;
import org.ametiste.redgreen.data.RedgreenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @since 0.1.0
 */
@RestController
@RequestMapping("/")
public class RedgreenRequestsController {

    @Autowired
    private FailoverLine simpleFailoverLine;

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
     * @return
     */
    @RequestMapping(value = "/{bundleName:.+}",
        method = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
    public Object performIncomingRequest(
            @PathVariable("bundleName") String bundleName,
            HttpServletRequest servletRequest) {

        return simpleFailoverLine.performRequest(bundleName, () -> {
            return new RedgreenRequest(
                    HttpMethod.valueOf(servletRequest.getMethod()),
                    servletRequest.getQueryString()
            );
        });
    }

}
