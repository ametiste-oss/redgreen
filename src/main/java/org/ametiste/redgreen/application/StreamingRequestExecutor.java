package org.ametiste.redgreen.application;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     High-performance request executor, this executor forwards a response
 *     body stream directly into client response.
 * </p>
 *
 * @since 0.1.0
 */
public class StreamingRequestExecutor implements RequestExecutor {

    public final static int DEFAULT_CONNECTION_TIMEOUT = 50;

    public final static int DEFAULT_READ_TIMEOUT = 300;

    private final int readTimeout;

    private final int connectionTimeoit;

    public StreamingRequestExecutor(int connectionTimeoit, int readTimeout) {
        this.readTimeout = readTimeout;
        this.connectionTimeoit = connectionTimeoit;
    }

    public StreamingRequestExecutor() {
        this(DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    @Override
    public  <T> T executeRequest(String method, String url, Forwarder<T> forwarder) {

        final HttpURLConnection connection = createConnection(url);
        setupConnection(method, connection);

        // TODO: what will happen, if the connection never closed?
        // I guess I need some cleanup or redesign it somehow.

        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return handleSucceccRequest(forwarder, connection);
            } else {
                connection.disconnect();
                throw new RuntimeException("Response was not OK.");
            }
        } catch (IOException e) {
            connection.disconnect();
            throw new RuntimeException("Can't read URL for forward.", e);
        } finally {
            // NOTE: YEAH, WE DON'T DISCONNECT CONEECTION, WE NEED THE OPENED STREAM!
        }
    }

    private void setupConnection(String method, HttpURLConnection connection) {
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new IllegalArgumentException("Illegal request method: " + method);
        }

        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectionTimeoit);
    }

    private <T> T handleSucceccRequest(Forwarder<T> forwarder, HttpURLConnection connection) {
        // NOTE: there is first http headers, dunno why, but it should be removed, so we rebuilding map

        final LinkedMultiValueMap<String, String> h =
                new LinkedMultiValueMap<>();

        for (Map.Entry<String, List<String>> hs : connection.getHeaderFields().entrySet()) {
            if (hs.getKey() == null) {
                continue;
            }
            h.put(hs.getKey(), hs.getValue());
        }

        return doStreamForward(forwarder, connection, h);
    }

    private HttpURLConnection createConnection(String url) {

        final HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            throw new RuntimeException("Can't establish connection.");
        }

        return connection;
    }

    private <T> T doStreamForward(Forwarder<T> forwarder, final HttpURLConnection connection, LinkedMultiValueMap<String, String> h) {

        final ForwardedResponse forwardedResponse = new ForwardedResponse() {

            @Override
            public void close() throws IOException {
                connection.disconnect();
            }

            @Override
            public void forwardTo(OutputStream outputStream) {
                try {
                    StreamUtils.copy(connection.getInputStream(), outputStream);
                } catch (IOException e) {
                    throw new RuntimeException("Can't forward stream.", e);
                } finally {
                    connection.disconnect();
                }
            }
        };

        return forwarder.forward(h, forwardedResponse);
    }

}
