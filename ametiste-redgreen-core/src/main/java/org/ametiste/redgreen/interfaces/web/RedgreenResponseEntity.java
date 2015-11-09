package org.ametiste.redgreen.interfaces.web;

import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

class RedgreenResponseEntity implements RedgreenResponse {

    private final HttpServletResponse servletResponse;
    private boolean wasForwarded = false;
    private boolean wasRedirected = false;
    private Object body;
    private Map<String, List<String>> headers;

    public RedgreenResponseEntity(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public ResponseEntity<Object> takeResponse() {

        if (wasRedirected) {
            return new ResponseEntity<Object>(null, HttpStatus.FOUND);
        }

        if (wasForwarded) {
            throw new IllegalStateException("RedgreenResponse was not forwarded.");
        }

        try {
            return new ResponseEntity<>(body,
                    new LinkedMultiValueMap<String, String>(headers), HttpStatus.OK);
        } finally {
            wasForwarded = true;
        }
    }

    @Override
    public void sendRedirect(String redirect) {
        try {
            servletResponse.sendRedirect(redirect);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            wasRedirected = true;
        }
    }

    @Override
    public void attachHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public void attachBody(Object body) {
        this.body = body;
    }

    @Override
    public void purge() {
        if (body instanceof Closeable) {
            try {
                ((Closeable) body).close();
            } catch (IOException e) {
                // NOTE: can't do anything, so ignore
            }
        }
    }
}
