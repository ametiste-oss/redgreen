package org.ametiste.redgreen.infrastructure;

import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public class DirectRedgreenBundleRepository implements RedgreenBundleRepostitory {

    private final HashMap<String, RedgreenBundle> bundles;

    public DirectRedgreenBundleRepository(Map<String, RedgreenBundle> bundles) {
        this.bundles = new HashMap<>(bundles);
    }

    @Override
    public RedgreenBundle loadBundle(String bundleName) {

        if (!bundles.containsKey(bundleName)) {
             throw new RuntimeException("Bundle with the given name does not exist: " + bundleName);
        }

        return bundles.get(bundleName);

        // return new RedgreenBundle("http://download.st.depositphotos.com/internal/publisher-dstr",
        //        Arrays.asList("http://download.st.depositphotos.com/internal/publisher-dph-stor")
        // );
    }
}