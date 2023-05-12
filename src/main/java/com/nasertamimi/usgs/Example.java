package com.nasertamimi.usgs;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Example {
    private static Logger logger = LogManager.getLogger(Example.class);

    public static void main( String[] args ) {
        try {
            LocalDate startDate = LocalDate.parse("2023-04-20");
            LocalDate endDate = LocalDate.parse("2023-04-20");

            USGSEarthquakeDownloader downloader = new USGSEarthquakeDownloader();

            LocalDate runDate = startDate;
            LocalDate nextDate = runDate.plusDays(1);

            while (runDate.isBefore(endDate) || runDate.isEqual(endDate)) {
                String runDateStr = runDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                String nextDateStr = nextDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

                logger.info(String.format("Run Date is %s", runDateStr));
                Path downloadedPath = downloader.download(runDateStr, nextDateStr);
                logger.info(String.format("Download completed for %s.", runDateStr));
                logger.info(String.format("Download Path is: %s", String.valueOf(downloadedPath)));

                logger.info(String.format("Writing TSV file started for %s", runDateStr));
                USGSTsvDataWriter tsvDataWriter = new USGSTsvDataWriter(downloadedPath);
                tsvDataWriter.write();

                runDate = nextDate;
                nextDate = nextDate.plusDays(1);
            }

        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage());
        }


    }
}
