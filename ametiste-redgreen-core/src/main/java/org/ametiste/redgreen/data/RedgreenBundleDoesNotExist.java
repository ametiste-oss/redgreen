package org.ametiste.redgreen.data;

/**
 * <p>
 *      Usually will be thrown by a {@link RedgreenBundleRepostitory} in cases
 *      where queried bundle can't be found or does not exist.
 * </p>
 *
 * @since 0.1.0
 */
public class RedgreenBundleDoesNotExist extends RuntimeException {

    private final String bundleName;

    public RedgreenBundleDoesNotExist(String message, String bundleName) {
        this(message, null, bundleName);
    }

    public RedgreenBundleDoesNotExist(String message, Throwable cause, String bundleName) {
        super(message, cause);
        this.bundleName = bundleName;
    }


}
