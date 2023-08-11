package com.nasertamimi.geosciences.datapipelines.core;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public abstract class Downloader {
    protected Properties props;
    public Downloader() {
        props = new Properties();
        try {
            InputStream in = Downloader.class.getResourceAsStream("/config.properties");
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public abstract Path download(String startDate, String endDate) throws Exception;
}
