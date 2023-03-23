package com.nasertamimi.earthquake;

import java.nio.file.Path;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class App {
    private static Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) {
        try {
            LocalDate localDate = LocalDate.now();
            String startDate = localDate.minusDays(1).toString();
            String endDate = localDate.toString();

            USGSDownloader downloader = new USGSDownloader();
            Path downloaded_path = downloader.download(startDate, endDate);

            logger.info("Download Path is: "+String.valueOf(downloaded_path));

        } catch (Exception e) {
            logger.error("An error occured: " + e.getMessage());
        }
    }
}
