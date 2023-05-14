package com.nasertamimi.geosciences.datapipelines;

import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Downloader {
    protected Properties props;
    public Downloader() {
        props = new Properties();
        try {
            FileInputStream in = new FileInputStream("src/main/resources/config.properties");
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public abstract Path download(String startDate, String endDate) throws Exception;
}
