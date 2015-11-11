package org.ametiste.redgreen.driver;

import org.ametiste.metrics.annotations.ErrorCountable;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.request.ResourceRequest;
import org.ametiste.redgreen.application.response.RedgreenResponse;
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

    public StreamingRequestDriver(int connectionTimeout, int readTimeout) {
        this.readTimeout = readTimeout;
        this.connectionTimeoit = connectionTimeout;
    }

    public StreamingRequestDriver() {
        this(DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    @Override
    @Timeable(name = StreamingRequestDriverMetric.EXECUTE_TIMING)
    @ErrorCountable(name = StreamingRequestDriverMetric.GENERAL_ERRORS_COUNT)
    public void executeRequest(ResourceRequest request, RedgreenResponse redgreenResponse) {

        final HttpURLConnection connection =
                request.connectResource(this::createConnection, this::setupConnection);

        //
        // TODO: what will happen, if the connection never closed?
        // I guess I need some cleanup or redesign it somehow.
        //
        // UPD 09.11.15:
        // Atm, purge() method for response added, now redgreen response clients
        // should invoke purge() method in case of errors.
        //
        // So, if ResponseBodyStream was attached to RedgreenResponse, it should be notified
        // about errors and purged.
        //
        // It should help to solve potential problems, but I still need to work on design.
        //

        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                handleSuccessRequest(redgreenResponse, connection);
            } else {
                connection.disconnect();
                throw new RuntimeException("Response was not OK.");
            }
        } catch (Exception e) {
            connection.disconnect();
            throw new RuntimeException("Can't read URL.", e);
        } finally {
            // NOTE: YEAH, WE DON'T DISCONNECT CONNECTION, WE NEED THE OPENED STREAM!
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
        redgreenResponse.attachHeaders(collectResponseHeaders(connection));
        redgreenResponse.attachBody(buildBodyStream(connection));
    }

    private LinkedMultiValueMap<String, String> collectResponseHeaders(HttpURLConnection connection) {
        final LinkedMultiValueMap<String, String> headers =
                new LinkedMultiValueMap<>();

        for (Map.Entry<String, List<String>> hs : connection.getHeaderFields().entrySet()) {
            // NOTE: there is first http headers, dunno why, but it should be removed, so we rebuilding map
            if (hs.getKey() == null) {
                continue;
            }
            headers.put(hs.getKey(), hs.getValue());
        }
        return headers;
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

    private ResponseBodyStream buildBodyStream(final HttpURLConnection connection) {
        return new ResponseBodyStream() {

            @Override
            public void close() throws IOException {
                connection.disconnect();
            }

            @Override
            public void writeBody(OutputStream outputStream) {
                try {
                    StreamUtils.copy(connection.getInputStream(), outputStream);
                } catch (IOException e) {
                    throw new RuntimeException("Can't forward stream.", e);
                } finally {
                    connection.disconnect();
                }
            }
        };
    }

}
