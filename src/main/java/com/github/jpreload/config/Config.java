package com.github.jpreload.config;

/**
 * Created by chenjunlong on 2018/8/1.
 */
public class Config {

    // prometheus yml path
    public static String YML_PATH = System.getProperty("prometheus.yml.path");

    // prometheus port
    public static String PROMETHEUS_PORT = System.getProperty("prometheus.port", "9090");

    // java-prometheus-reload port
    public static String PORT = System.getProperty("port", "9091");
}
