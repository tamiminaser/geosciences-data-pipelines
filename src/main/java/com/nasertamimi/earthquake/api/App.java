package com.nasertamimi.earthquake.api;

import java.net.HttpURLConnection;

public class App {
    public static void main( String[] args ) {
        try {
            GeoConn geoConn = new GeoConn();
            Request request = new Request();

            String response = request.perform(geoConn.usgs());

            System.out.println(response);

        } catch (Exception e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }
}
