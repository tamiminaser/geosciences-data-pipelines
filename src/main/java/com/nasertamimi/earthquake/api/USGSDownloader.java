package com.nasertamimi.earthquake.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class USGSDownloader extends Downloader{
    public USGSDownloader(){
        super();
    }

    private static Logger logger = LogManager.getLogger(USGSDownloader.class);

    public Path download(String startDate, String endDate) throws Exception{
        try {
            GeoConn geoConn = new GeoConn();
            Request request = new Request();

            String response = request.perform(geoConn.usgs(startDate, endDate));

            logger.info(response);

            Path path = Paths.get(String.format("/tmp/output_%s.json", LocalDate.now()));
            Files.writeString(path, response,  StandardCharsets.UTF_8);
            return path;
        } catch (Exception e)
        {
            logger.error("An error occured: " + e.getMessage());
            return null;
        }
    }
}
