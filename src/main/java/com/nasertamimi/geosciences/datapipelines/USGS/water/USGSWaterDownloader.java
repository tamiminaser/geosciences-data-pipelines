package com.nasertamimi.geosciences.datapipelines.USGS.water;

import com.nasertamimi.geosciences.datapipelines.core.Downloader;
import com.nasertamimi.geosciences.datapipelines.core.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class USGSWaterDownloader extends Downloader {
    public USGSWaterDownloader(){
        super();
    }

    private static Logger logger = LogManager.getLogger(USGSWaterDownloader.class);

    public Path download(String startDate, String endDate) throws IOException {

        try {
            WaterConn waterConn = new WaterConn();
            Request request = new Request();

            String response = request.perform(waterConn.create(startDate, endDate));

            //logger.info(response);

            long runTime = System.currentTimeMillis(); //UTC Linux time in milliseconds

            String baseDownloadPath = props.getProperty("waterDownloadBasePath");

            Path path = Paths.get(String.format("%s/start_date=%s/", baseDownloadPath, startDate));

            Files.createDirectories(path);
            Path filepath = path.resolve(Paths.get("output.json"));
            Files.writeString(filepath, response,  StandardCharsets.UTF_8);

            return path;
        } catch (Exception e)
        {
            logger.error("An error occurred: " + e.getMessage());
            return null;
        }
    }
}
