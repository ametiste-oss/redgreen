package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.RedgreenComponentsFactory;
import org.ametiste.redgreen.bundle.*;
import org.ametiste.redgreen.driver.StreamingRequestDriverFactory;
import org.ametiste.redgreen.hystrix.configuration.HystrixSimpleFailoverLineConfiguration;
import org.ametiste.redgreen.infrastructure.DirectRedgreenBundleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.function.Supplier;

/**
 * <p>
 *     Defines the set of properties that could be used to configure
 *     {@link DirectRedgreenBundleRepository} instance.
 * </p>
 * <p>
 *     Defined properties set is prefixed by the <i>redgreen.direct</i>.
 * </p>
 * <p>
 *     This properties set defines convenient way to configure bundles which would be stored into
 *     {@code repository}, using <i>application.properties</i> file.
 * </p>
 *
 * <p>
 *     Global properties format is:
 *     <br>
 *     <br>
 *     redgreen.direct.defaultConnectionTimeout=[TIMEOUT VALUE]
 *     redgreen.direct.defaultReadTimeout=[TIMEOUT VALUE]
 * </p>
 *
 * <p>
 *     Bundle properties format is:
 *     <br>
 *     <br>
 *     redgreen.direct.bundles.<i>[BUNDLE NAME]</i>.<i>[red|green]</i>=[RESOURCE URI]
 *     <br>
 *     redgreen.direct.bundles.<i>[BUNDLE NAME]</i>.<i>[line]</i>=[LINE NAME]
 *     <br>
 *     redgreen.direct.bundles.<i>[BUNDLE NAME]</i>.<i>[readTimeout]</i>=[timeout value]
 *     <br>
 *     redgreen.direct.bundles.<i>[BUNDLE NAME]</i>.<i>[conncetionTimeout]</i>=[timeout value]
 * </p>
 *
 * <p>
 *     Note, the <b>green</b> resource <i>may</i> have multiple URIs attached, but only first will be used,
 *     and the <b>red</b> resource <i>may</i> have multiple URIS attached, and each of them <i>may</i>
 *     be used during failover line processing.
 * </p>
 *
 * <p>
 *     <i>line</i> configuration property is optional, by default 'hystrixSimpleLine' is used as line of
 *     bundle execution.
 * </p>
 *
 * <p>
 *     <i>connectionTimeout</i> configuration property is optional, this property defines connection
 *     timeout value for each resoce in a configured bundle, defined timeout will be applied
 *     for each request to bundle resources. By default this property value is 300ms.
 *     Note, if the {@code redgreen.direct.defaultConnectionTimeout} property is defined, then its value
 *     will be used as default.
 * </p>
 * <p>
 *     <i>readTimeout</i> configuration property is optional, this property defines read timeout value
 *     for each resoce in a configured bundle, defined timeout will be applied for each request to bundle
 *     resources. By default this property value is 1500ms.
 *     Note, if the {@code redgreen.direct.defaultReadTimeout} property is defined, then its value
 *     will be used as default
 * </p>
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

    private final static String DEFAULT_LINE =
            HystrixSimpleFailoverLineConfiguration.LINE_FACTORY_NAME;

    private final static String DEFAULT_DRIVER =
            StreamingRequestDriverFactory.DRIVER_FACTORY_NAME;

    private final static int DEFAULT_CONNECTION_TIMEOUT = 300;

    private final static int DEFAULT_READ_TIMEOUT = 1500;

    private final static String DEFAULT_ERROR_RESOURCE = "http://localhost/error";

    @Autowired
    private RedgreenComponentsFactory redgreenFactory;

    private Map<String, Map<String, List<String>>> bundles = new HashMap<>();

    private int defaultConnectionTimeout;

    private int defaultReadTimeout;

    private Map<String, String> errorBundle;

    public Map<String, Map<String, List<String>>> getBundles() {
        return bundles;
    }

    public Map<String, String> getErrorBundle() {
        return errorBundle;
    }

    public int getDefaultConnectionTimeout() {
        return defaultConnectionTimeout;
    }

    public int getDefaultReadTimeout() {
        return defaultReadTimeout;
    }

    public void setDefaultConnectionTimeout(int defaultConnectionTimeout) {
        this.defaultConnectionTimeout = defaultConnectionTimeout;
    }

    public void setErrorBundle(Map<String, String> errorBundle) {
        this.errorBundle = errorBundle;
    }

    public void setDefaultReadTimeout(int defaultReadTimeout) {
        this.defaultReadTimeout = defaultReadTimeout;
    }

    public List<Bundle> getComposedBundles() {

        final List<Bundle> composed = new ArrayList<>(bundles.size());

        bundles.forEach((k, v) -> {

                    // NOTE: naive configuration parser, that hopes the config has layout like:
                    //  .. .bundles.NAME.line=simpleHystrix
                    //  .. .bundles.NAME.green=..
                    //  .. .bundles.NAME.red=..
                    //  .. .bundles.NAME.connectionTimeout=..
                    //  .. .bundles.NAME.readTimeout=..

                    final Bundle redgreenBundle = new RedgreenBundle(
                            new RedgreenPair(k,
                                v.get("green").get(0),
                                v.get("red"),
                                singleIntValue(v, "connectionTimeout", this::connectionTimeoutValue),
                                singleIntValue(v, "readTimeout", this::readTimeoutValue)
                            ),
                            redgreenFactory.createFailoverLine(singleValue(v, "line", DEFAULT_LINE)),
                            redgreenFactory.createRequestDriver(singleValue(v, "driver", DEFAULT_DRIVER))
                    );

                    composed.add(redgreenBundle);

                }
        );

        // composed.add(new CachingBundle( composed.get(0), new InMemoryCacheBundle()));

        return composed;
    }

    public Bundle getComposedErrorBundle() {
        return new RedirectingErrorResourceBundle(
                errorBundle.getOrDefault("errorResource", DEFAULT_ERROR_RESOURCE)
        );
    }

    // TODO: move to factory
    public Bundle getComposedSingleErrorResourceBundle() {
        return new SingleErrorResourceBundle(
            errorBundle.getOrDefault("errorResource", DEFAULT_ERROR_RESOURCE),
            redgreenFactory.createRequestDriver("simpleStreamDriver"),
            intValue(errorBundle, "connectTimeout", this::connectionTimeoutValue),
            intValue(errorBundle, "readTimeout", this::readTimeoutValue)
        );
    }

    private int readTimeoutValue() {
        return defaultReadTimeout == 0 ? DEFAULT_READ_TIMEOUT : defaultReadTimeout;
    }

    private int connectionTimeoutValue() {
        return defaultConnectionTimeout == 0 ? DEFAULT_CONNECTION_TIMEOUT : defaultConnectionTimeout;
    }

    private static String singleValue(Map<String, List<String>> map, String name, String defaultValue) {
        return map.getOrDefault(name, Arrays.asList(defaultValue)).get(0);
    }

    private static Integer singleIntValue(Map<String, List<String>> map, String name, Supplier<Integer> defaultValue) {
        // NOTE: yeah, I know, is unreadable :[ but it is simpliest
        // way to unify possibly multiple properties parsing
        return Integer.parseInt(map.getOrDefault(name, Arrays.asList(Integer.toString(defaultValue.get()))).get(0));
    }

    private static Integer intValue(Map<String, String> map, String name, Supplier<Integer> defaultValue) {
        return Integer.parseInt(map.getOrDefault(name, Integer.toString(defaultValue.get())));
    }

}
