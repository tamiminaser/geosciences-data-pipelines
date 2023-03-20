package com.nasertamimi.earthquake.api;

import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;


public class App {
    public static void main( String[] args ) {
        try {
            GeoConn geoConn = new GeoConn();
            Request request = new Request();

            LocalDate localDate = LocalDate.now();
            String startDate = localDate.minusDays(1).toString();
            String endDate = localDate.toString();
            String response = request.perform(geoConn.usgs(startDate, endDate));

            System.out.println(response);

            Path path = Paths.get(String.format("/tmp/earthquakeApi/output_%s.json", LocalDate.now()));
            Files.writeString(path, response,  StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }
}
