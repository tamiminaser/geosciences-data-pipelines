package com.nasertamimi.earthquake;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class Task {
    private static Logger logger = LogManager.getLogger(Task.class);

    public static void main( String[] args ) {
        try {
            String startDate = System.getProperty("start_date");
            String endDate = System.getProperty("end_date");

            logger.info(String.format("Start Date is %s", startDate));
            logger.info(String.format("End Date is %s", endDate));

            USGSDownloader downloader = new USGSDownloader();
            Path downloadedPath = downloader.download(startDate, endDate);

            logger.info("Download completed.");
            logger.info("Download Path is: "+String.valueOf(downloadedPath));

            logger.info("Writing TSV file started.");
            USGSTsvDataWriter tsvDataWriter = new USGSTsvDataWriter(downloadedPath);
            tsvDataWriter.write();

        } catch (Exception e) {
            logger.error("An error occured: " + e.getMessage());
        }
    }
}
