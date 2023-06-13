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
            } else if (downloadType == "fire") {
                downloader = new FIRMSFireDownloader();
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

                if (downloadType == "earthquake") {
                    logger.info(String.format("Writing TSV file started for %s", runDateStr));
                    USGSEarthquakeDataWriter earthquakeDataWriter = new USGSEarthquakeDataWriter(downloadedPath);
                    earthquakeDataWriter.write("tsv");
                } else if (downloadType == "fire") {
                    logger.info(String.format("CSV file for %s is written to %s", runDateStr, downloadedPath));
                }

                runDate = nextDate;
                nextDate = nextDate.plusDays(1);
            }

        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage());
        }
    }
}
