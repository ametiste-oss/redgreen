package org.ametiste.redgreen.application;

import java.io.Closeable;
import java.io.OutputStream;

/**
 *
 * @since
 */
public interface ForwardedResponse extends Closeable {

    /**
     * Note, implementation MUST close underlying response ( in any mean ),
     * in both cases - successful and failed forwards.
     *
     * @param outputStream stream to write response
     */
    void forwardTo(OutputStream outputStream);

}
