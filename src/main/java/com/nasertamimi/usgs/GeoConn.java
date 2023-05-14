package com.nasertamimi.usgs;

import java.net.URL;
import java.net.HttpURLConnection;

public class GeoConn {
    public GeoConn(){
    }
    public HttpURLConnection usgs(String startDate, String endDate) throws Exception{

        String endpoint = String.format("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=%s&endtime=%s", startDate, endDate);

        // creating a connection
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return  conn;
    }
}