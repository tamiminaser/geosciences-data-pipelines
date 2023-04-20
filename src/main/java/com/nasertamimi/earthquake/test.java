package com.nasertamimi.earthquake;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.JSONArray;

public class test {
    public static void main(String[] args) {

        try {

            GeoConn geoConn = new GeoConn();
            Request request = new Request();

            String response = request.perform(geoConn.usgs("2023-03-29", "2023-03-30"));

            JSONArray jsonResponse = new JSONArray(response);

            System.out.println(jsonResponse);

            /*
            ArrayList<String> myArray = new ArrayList<String>();
            myArray.add("1,2,3");
            myArray.add("4,5,6");

            String output = myArray.stream().collect(Collectors.joining("\n"));
            System.out.println(output);

            FileWriter writer = new FileWriter("file.csv");
            writer.write(output);
            writer.close();
             */
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
