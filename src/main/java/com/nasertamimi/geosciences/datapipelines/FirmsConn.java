package com.nasertamimi.geosciences.datapipelines;

import java.net.HttpURLConnection;
import java.net.URL;


public class FirmsConn {
    private final String mapKey = System.getenv("MAP_KEY");
    public FirmsConn(){
    }
    public HttpURLConnection create(String startDate) throws Exception{

        String endpoint = String.format("https://firms.modaps.eosdis.nasa.gov/api/area/csv/%s/VIIRS_SNPP_NRT/world/1/%s", mapKey, startDate);

        // creating a connection
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }
}