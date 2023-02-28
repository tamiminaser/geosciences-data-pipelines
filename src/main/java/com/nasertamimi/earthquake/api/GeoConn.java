package com.nasertamimi.earthquake.api;

import java.net.URL;
import java.net.HttpURLConnection;

public class GeoConn {
    public GeoConn(){
    }
    public HttpURLConnection usgs() throws Exception{
        String endpoint = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2023-02-27&endtime=2023-02-28";

        // creating a connection
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return  conn;
    }
}
