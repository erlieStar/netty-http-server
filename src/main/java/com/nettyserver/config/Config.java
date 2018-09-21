package com.nettyserver.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config {

    private static Properties properties = new Properties();
    private static final String configName = "config.properties";

    static {

        try {
            properties.load(new InputStreamReader(Config.class.getClassLoader().getResourceAsStream(configName), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getInt(String str) {
        return Integer.parseInt(properties.getProperty(str));
    }

    public static long getLong(String str) {
        return Long.parseLong(properties.getProperty(str));
    }

    public static String getString(String str) {
        return properties.getProperty(str);
    }
}
