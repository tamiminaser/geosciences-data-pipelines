package com.nasertamimi.geosciences.datapipelines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private static Logger logger = LogManager.getLogger(Task.class);

    public static void main( String[] args ) {
        try {
            LocalDate startDate = LocalDate.parse(System.getProperty("start_date"));
            LocalDate endDate = LocalDate.parse(System.getProperty("end_date"));
            String downloadType = System.getProperty("download_type");

            Downloader downloader = null;

            if (downloadType == "earthquake") {
                downloader = new USGSEarthquakeDownloader();
            } else if (downloadType == "water") {
                downloader = new USGSWaterDownloader();
            }

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
                USGSEarthquakeDataWriter tsvDataWriter = new USGSEarthquakeDataWriter(downloadedPath);
                tsvDataWriter.write();

                runDate = nextDate;
                nextDate = nextDate.plusDays(1);
            }

        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage());
        }
    }
}
