package com.nasertamimi.geosciences.datapipelines.core;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

public abstract class Downloader {
    protected Properties props;
    public Downloader() {
        props = loadProperties("config.properties");
    }
    public abstract Path download(String startDate, String endDate) throws Exception;

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            // Load the properties file using the ClassLoader
            InputStream inputStream = Downloader.class.getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
