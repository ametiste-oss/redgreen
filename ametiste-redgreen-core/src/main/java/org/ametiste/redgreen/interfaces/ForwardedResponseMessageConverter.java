package org.ametiste.redgreen.interfaces;

import org.ametiste.metrics.annotations.Chronable;
import org.ametiste.metrics.annotations.ErrorCountable;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class ForwardedResponseMessageConverter extends AbstractHttpMessageConverter<ForwardedResponse> {

    public ForwardedResponseMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return ForwardedResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected ForwardedResponse readInternal(Class<? extends ForwardedResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new RuntimeException("Not supported yet.");
    }

    @Override
    protected void writeInternal(ForwardedResponse inputStream, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        inputStream.forwardTo(outputMessage.getBody());
    }
}