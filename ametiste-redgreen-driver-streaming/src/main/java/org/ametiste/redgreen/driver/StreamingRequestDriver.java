package org.ametiste.redgreen.driver;

import org.ametiste.redgreen.application.response.ForwardedResponse;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.ResourceRequest;
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
 *     body stream directly into client response forwarder.
 * </p>
 *
 * @since 0.1.1
 */
public class StreamingRequestDriver implements RequestDriver {

    public final static int DEFAULT_CONNECTION_TIMEOUT = 600;

    public final static int DEFAULT_READ_TIMEOUT = 500;

    private final int readTimeout;

    private final int connectionTimeoit;

    public StreamingRequestDriver(int connectionTimeoit, int readTimeout) {
        this.readTimeout = readTimeout;
        this.connectionTimeoit = connectionTimeoit;
    }

    public StreamingRequestDriver() {
        this(DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    @Override
    public  void executeRequest(ResourceRequest request, RedgreenResponse redgreenResponse) {

        final HttpURLConnection connection =
                request.connectResource(this::createConnection, this::setupConnection);

        // TODO: what will happen, if the connection never closed?
        // I guess I need some cleanup or redesign it somehow.

        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                handleSuccessRequest(redgreenResponse, connection);
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

    private void setupConnection(HttpURLConnection connection, ResourceRequest.Options options) {
        try {
            connection.setRequestMethod(options.method);
        } catch (ProtocolException e) {
            throw new IllegalArgumentException("Illegal request method: " + options.method);
        }

        connection.setReadTimeout(options.rTimeout);
        connection.setConnectTimeout(options.cTimeout);
    }

    private void handleSuccessRequest(RedgreenResponse redgreenResponse, HttpURLConnection connection) {
        // NOTE: there is first http headers, dunno why, but it should be removed, so we rebuilding map

        final LinkedMultiValueMap<String, String> h =
                new LinkedMultiValueMap<>();

        for (Map.Entry<String, List<String>> hs : connection.getHeaderFields().entrySet()) {
            if (hs.getKey() == null) {
                continue;
            }
            h.put(hs.getKey(), hs.getValue());
        }

        doStreamForward(redgreenResponse, connection, h);
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

    private void doStreamForward(RedgreenResponse redgreenResponse, final HttpURLConnection connection, LinkedMultiValueMap<String, String> h) {

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

        redgreenResponse.forward(h, forwardedResponse);
    }

}
