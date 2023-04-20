package com.nasertamimi.earthquake;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

            //logger.info(response);

            long runTime = System.currentTimeMillis(); //UTC Linux time in milliseconds

            Path path = Paths.get(
                    String.format("/tmp/earthquakeAPI/source=USGS/start_date=%s/end_date=%s/run_time=%d",
                            startDate, endDate, runTime));

            Files.createDirectories(path);
            Files.writeString(path.resolve(Paths.get("output.json")), response,  StandardCharsets.UTF_8);

            return path;
        } catch (Exception e)
        {
            logger.error("An error occured: " + e.getMessage());
            return null;
        }
    }
}
