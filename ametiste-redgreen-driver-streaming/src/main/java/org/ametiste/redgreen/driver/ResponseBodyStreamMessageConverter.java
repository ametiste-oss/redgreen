package org.ametiste.redgreen.driver;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class ResponseBodyStreamMessageConverter extends AbstractHttpMessageConverter<ResponseBodyStream> {

    public ResponseBodyStreamMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return ResponseBodyStream.class.isAssignableFrom(clazz);
    }

    @Override
    protected ResponseBodyStream readInternal(Class<? extends ResponseBodyStream> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new RuntimeException("Not supported yet.");
    }

    @Override
    protected void writeInternal(ResponseBodyStream inputStream, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        inputStream.writeBody(outputMessage.getBody());
    }
}