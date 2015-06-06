package org.ametiste.redgreen.data;

import org.ametiste.redgreen.application.line.FailoverLine;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <p>
 *     Resources {@code bundle}, this object describes a pair of <i>red-green</i> resources
 *     which describes links by on {@code bundeled} resource may be found.
 * </p>
 *
 * <p>
 *     Say, if we want to {@code bundle} <i>google</i> results set resource, we can achieve it
 *     by defining the {@code bundle} that contains URI to <i>http://google.com/q</i> and
 *     <i>http://en.google.com/q</i>:
 *     <br>
 *     <pre>
 *         -- name: google.bundle
 *          - green: http://google.com/q
 *          - red: http://en.google.com/q
 *     </pre>
 *
 *     This defintion will describe the bundle named <i>google.bundle</i>, and will contain the given
 *     <i>green</i> and <i>red</i> resources. {@link FailoverLine} implementations will use
 *     these resources to try perform requests to <i>google.bundle</i>.
 * </p>
 *
 * <p>
 *     Each {@code bundle} should has unique name.
 * </p>
 *
 * <p>
 *     The first element of a bundle - <i>green</i> resource, is a resource
 *     that defined as primary resource of a bundle, usually it's points
 *     to recent version of resource, or
 * </p>
 *
 * @see FailoverLine
 * @since 0.1.0
 */
public class RedgreenBundleDescription {

    private final String name;

    private final String line;

    private final String greenResource;

    private final List<String> redResources;

    public RedgreenBundleDescription(String name, String line, String greenResource, List<String> redResources) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Bundle name can't be null nor empty.");
        }

        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Line name can't be null nor empty.");
        }

        if (greenResource == null || name.isEmpty()) {
            throw new IllegalArgumentException("Green resource URI can't be null nor empty.");
        }

        if (redResources == null || redResources.isEmpty()) {
            throw new IllegalArgumentException("Red resources URI list can't be null nor empty.");
        }

        this.name = name;
        this.line = line;
        this.greenResource = greenResource;
        this.redResources = redResources;
    }

    /**
     * <p>
     *     Provides <i>green</i> resource URI.
     * </p>
     *
     * @return {@code bundle} name, can't be {@code null}
     * @since 0.1.0
     */
    public String greenResource() {
        return greenResource;
    }

    /**
     * <p>
     *     Apply the given {@code Function} to each <i>red</i> resource within the bundle.
     * </p>
     *
     * @return mapped {@code Function} result, depends on the {@code Function} type.
     * @since 0.1.0
     */
    public <T> Stream<T> mapRedResources(Function<String, T> resourceProcessor) {
        return redResources.stream().map(resourceProcessor);
    }

    /**
     * <p>
     *     Provides {@code bundle} name.
     * </p>
     *
     * @return {@code bundle} name, can't be {@code null}
     * @since 0.1.0
     */
    public String name() {
        return name;
    }

    public String line() {
        return line;
    }
}
