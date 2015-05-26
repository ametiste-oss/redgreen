package org.ametiste.redgreen.data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @since
 */
public class RedgreenBundle {

    private final String greenResource;
    private final List<String> redResources;

    public RedgreenBundle(String greenResource, List<String> redResources) {
        this.greenResource = greenResource;
        this.redResources = redResources;
    }

    public String greenResource() {
        return greenResource;
    }

    public <T> Stream<T> mapRedResources(Function<String, T> resourceProcessor) {
        return redResources.stream().map(resourceProcessor);
    }

}
