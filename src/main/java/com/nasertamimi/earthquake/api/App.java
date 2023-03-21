package com.nasertamimi.earthquake.api;

import java.nio.file.Path;
import java.time.LocalDate;


public class App {
    public static void main( String[] args ) {
        try {
            LocalDate localDate = LocalDate.now();
            String startDate = localDate.minusDays(1).toString();
            String endDate = localDate.toString();

            USGSDownloader downloader = new USGSDownloader();
            Path downloaded_path = downloader.download(startDate, endDate);

        } catch (Exception e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }
}
