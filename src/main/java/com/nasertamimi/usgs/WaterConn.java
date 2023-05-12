package com.nasertamimi.usgs;

import java.net.HttpURLConnection;
import java.net.URL;

public class WaterConn {
    public WaterConn(){
    }
    public HttpURLConnection usgs(String startDate, String endDate) throws Exception{
        String endpoint = String.format("https://waterservices.usgs.gov/nwis/iv/?format=json&stateCd=tx&startDT=%s&endDT=%s&parameterCd=00060,00065&siteStatus=all", startDate, endDate);

        // creating a connection
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return  conn;
    }
}
