package com.nasertamimi.earthquake.api;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class USGSDownloader extends Downloader{
    public USGSDownloader(){
        super();
    }
    public Path download(String startDate, String endDate) throws Exception{
        try {
            GeoConn geoConn = new GeoConn();
            Request request = new Request();

            String response = request.perform(geoConn.usgs(startDate, endDate));

            System.out.println(response);

            Path path = Paths.get(String.format("/tmp/earthquakeApi/output_%s.json", LocalDate.now()));
            Files.writeString(path, response,  StandardCharsets.UTF_8);

            return path;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
