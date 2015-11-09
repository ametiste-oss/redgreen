package org.ametiste.redgreen.driver;

import java.io.Closeable;
import java.io.OutputStream;

/**
 *
 * @since
 */
public interface ResponseBodyStream extends Closeable {

    /**
     * Note, implementation MUST close underlying response ( in any mean ),
     * in both cases - successful and failed forwards.
     *
     * @param outputStream stream to write response
     */
    void writeBody(OutputStream outputStream);

}
