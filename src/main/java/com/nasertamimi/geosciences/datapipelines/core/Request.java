package com.nasertamimi.geosciences.datapipelines.core;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Request {
    public Request(){
    }
    public String perform(HttpURLConnection conn) throws Exception{
        // send the request
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine+"\n");
        }
        in.close();
        return response.toString();
    }
}
