package org.ametiste.redgreen.data;

import org.ametiste.redgreen.data.RedgreenBundle;

/**
 * <p>
 *  Defines protocol of repository that contains {@link RedgreenBundle}, and
 *  provides access to registered {@code bundles}.
 * </p>
 *
 * @since 0.1.0
 */
public interface RedgreenBundleRepostitory {

    /**
     * <p>
     *     Loads a {@code bundle} by its name. If the named bundle can't be found,
     *     exception will be thrown.
     * </p>
     *
     * @param bundleName name of a {@code bundle} to load, must be {@code not null}
     * @return found {@code bundle} object, can't be {@code null}.
     * @throws RedgreenBundleDoesNotExist if the named bundle can't be found or does not exist.
     */
    RedgreenBundle loadBundle(String bundleName) throws RedgreenBundleDoesNotExist;

}
