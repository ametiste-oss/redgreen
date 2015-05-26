package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.data.RedgreenBundle;
import org.ametiste.redgreen.infrastructure.DirectRedgreenBundleRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     Defines the set of properties that could be used to configure
 *     {@link DirectRedgreenBundleRepository} instance.
 * </p>
 * <p>
 *    Defined properties set is prefixed by the <i>redgreen.direct</i>.
 * </p>
 * <p>
 *     This properties set defines convenient way to configure {@code repository}
 *     consistency via <i>application.properties</i>.
 * </p>
 *
 * <p>
 *     Properties format is:
 *     <br>
 *     <br>
 *     redgreen.direct.bundles.<i>[BUNDLE NAME]</i>.<i>[red|green]</i>=[RESOURCE URI]
 * </p>
 *
 * <p>
 *     Note, the <b>green</b> resource <i>may</i> have multiple URIs attached, but only first will be used,
 *     and the <b>red</b> resource <i>may</i> have multiple URIS attached, and each of them <i>may</i>
 *     be used during failover line processing.
 * </p>
 *
 * <p>
 *     For example, properties:
 *     <br>
 *     <br>
 *     redgreen.direct.bundles.<b>example</b>.<b>green</b>=http://example.com/test/resource
 *     redgreen.direct.bundles.<b>example</b>.<b>red</b>=http://example.org/test/failover/resource,
 *     http://example.org/test/failover/resource
 *     <br>
 *     <br>
 *     Will configure the <i>'example'</i> bundle, which main resource is <i>http://example.com/test/resource</i>,
 *     and failover resources are <i>http://example.org/test/failover/resource,http://example.org/test/failover/resource</i>.
 * </p>
 *
 * @since 0.1.0
 */
@ConfigurationProperties("redgreen.direct")
public class DirectRedgreenBundleRepositoryProperties {

    private Map<String, Map<String, List<String>>> bundles = new HashMap<>();

    public Map<String, Map<String, List<String>>> getBundles() {
        return bundles;
    }

    public HashMap<String, RedgreenBundle> getComposedBundles() {

        final HashMap<String, RedgreenBundle> composed = new HashMap<>();

        bundles.forEach(
                (k, v) -> {
                    composed.put(k, new RedgreenBundle(v.get("green").get(0), v.get("red")));
                }
        );

        return composed;
    }

}
