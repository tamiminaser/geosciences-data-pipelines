package com.nasertamimi.earthquake;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.LocalDate;



public class Example {
    private static Logger logger = LogManager.getLogger(Example.class);

    public static void main( String[] args ) {
        try {
            LocalDate localDate;
            localDate = LocalDate.now();
            String startDate = localDate.minusDays(1).toString();
            String endDate = localDate.toString();

            USGSDownloader downloader = new USGSDownloader();
            Path downloadedPath = downloader.download(startDate, endDate);

            USGSTsvFileWriter TSVfileWriter = new USGSTsvFileWriter(downloadedPath);
            TSVfileWriter.write();

        } catch (Exception e) {
            logger.error("An error occured: " + e.getMessage());
        }


    }
}
