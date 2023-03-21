package com.nasertamimi.earthquake.api;

import java.nio.file.Path;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        try {
            LocalDate localDate = LocalDate.now();
            String startDate = localDate.minusDays(1).toString();
            String endDate = localDate.toString();

            USGSDownloader downloader = new USGSDownloader();
            Path downloaded_path = downloader.download(startDate, endDate);

            logger.info(String.valueOf(downloaded_path));

        } catch (Exception e) {
            System.out.println("An error occured: " + e.getMessage());
        }
        logger.info("Hello WORLD!");
    }
}
