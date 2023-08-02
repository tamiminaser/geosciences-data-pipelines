package com.nasertamimi.geosciences.datapipelines.USGS.earthquake;

import com.nasertamimi.geosciences.datapipelines.core.Downloader;
import com.nasertamimi.geosciences.datapipelines.core.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class USGSEarthquakeDownloader extends Downloader {
    public USGSEarthquakeDownloader(){
        super();
    }

    private static Logger logger = LogManager.getLogger(USGSEarthquakeDownloader.class);

    public Path download(String startDate, String endDate) throws Exception{
        try {
            GeoConn geoConn = new GeoConn();
            Request request = new Request();

            String response = request.perform(geoConn.create(startDate, endDate));

            //logger.info(response);

            long runTime = System.currentTimeMillis() / 1000L; //Unix epoch time in seconds

            String baseDownloadPath = props.getProperty("earthquakeDownloadBasePath");

            Path path = Paths.get(String.format("%s/%s/start_date=%s/", baseDownloadPath, runTime, startDate));

            Files.createDirectories(path);
            Path filepath = path.resolve(Paths.get("output.csv"));
            Files.writeString(filepath, response,  StandardCharsets.UTF_8);

            return path;
        } catch (Exception e)
        {
            logger.error("An error occurred: " + e.getMessage());
            return null;
        }
    }
}
