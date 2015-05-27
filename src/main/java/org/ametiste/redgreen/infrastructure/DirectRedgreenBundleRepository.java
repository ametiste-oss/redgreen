package org.ametiste.redgreen.infrastructure;

import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenBundleDoesNotExist;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *      Simpliest {@link RedgreenBundleRepostitory} implementations, this repository provides access
 *      to {@link RedgreenBundle} objects that would be registering during the repository construction.
 * </p>
 *
 * <p>
 *      This repository implementation does not provide any methods to
 *      change its state, so it's thread-safe.
 * </p>
 *
 * <p>
 *      This repository could be used to build simple application configuration
 *      variant.
 * </p>
 *
 * @since 0.1.0
 */
public class DirectRedgreenBundleRepository implements RedgreenBundleRepostitory {

    private final HashMap<String, RedgreenBundle> bundles = new HashMap<>();

    public DirectRedgreenBundleRepository(List<RedgreenBundle> bundles) {
        bundles.forEach(
                (b) -> this.bundles.put(b.name(), b)
        );
    }

    @Override
    public RedgreenBundle loadBundle(String bundleName) throws RedgreenBundleDoesNotExist{

        if (!bundles.containsKey(bundleName)) {
             throw new RedgreenBundleDoesNotExist(
                     "Bundle with the given name does not exist: " + bundleName, bundleName);
        }

        return bundles.get(bundleName);

    }
}