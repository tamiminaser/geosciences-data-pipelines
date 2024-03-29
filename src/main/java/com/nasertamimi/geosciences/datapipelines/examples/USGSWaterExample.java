package com.nasertamimi.geosciences.datapipelines.examples;

import com.nasertamimi.geosciences.datapipelines.core.Downloader;
import com.nasertamimi.geosciences.datapipelines.USGS.water.USGSWaterDownloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class USGSWaterExample {
    private static Logger logger = LogManager.getLogger(USGSWaterExample.class);

    public static void main( String[] args ) {
        try {
            LocalDate startDate = LocalDate.parse("2023-04-20");
            LocalDate endDate = LocalDate.parse("2023-04-20");

            Downloader downloader = new USGSWaterDownloader();

            LocalDate runDate = startDate;
            LocalDate nextDate = runDate.plusDays(1);

            while (runDate.isBefore(endDate) || runDate.isEqual(endDate)) {
                String runDateStr = runDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                String nextDateStr = nextDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

                logger.info(String.format("Run Date is %s", runDateStr));
                Path downloadedPath = downloader.download(runDateStr, nextDateStr);
                logger.info(String.format("Download completed for %s.", runDateStr));
                logger.info(String.format("Download Path is: %s", String.valueOf(downloadedPath)));

                runDate = nextDate;
                nextDate = nextDate.plusDays(1);
            }
        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage());
        }


    }
}
