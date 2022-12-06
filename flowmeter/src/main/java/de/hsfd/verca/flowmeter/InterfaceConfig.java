package de.hsfd.verca.flowmeter;

public abstract class InterfaceConfig {

    public static final int snaplen = 64 * 1024;//2048; // Truncate packet at this size
    public static final int timeout = 10 * 1000; // In milliseconds

}
