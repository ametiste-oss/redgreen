package org.ametiste.redgreen.bundle;

import java.util.List;

/**
 *
 * @since
 */
public class RedgreenPair {

    private final String name;

    private final String green;

    private final List<String> red;

    private final int cTimeout;

    private final int rTimeout;

    public RedgreenPair(String name, String green, List<String> red, int cTimeout, int rTimeout) {
        this.name = name;
        this.green = green;
        this.red = red;
        this.cTimeout = cTimeout;
        this.rTimeout = rTimeout;
    }

    public String getName() {
        return name;
    }

    public String getGreen() {
        return green;
    }

    public List<String> getRed() {
        return red;
    }

    public int getcTimeout() {
        return cTimeout;
    }

    public int getrTimeout() {
        return rTimeout;
    }
}
