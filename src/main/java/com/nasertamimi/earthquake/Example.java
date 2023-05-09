package com.nasertamimi.earthquake;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;



public class Example {
    private static Logger logger = LogManager.getLogger(Example.class);

    public static void main( String[] args ) {
        try {
            LocalDate localDate;
            localDate = LocalDate.now();
            String startDate = localDate.minusDays(1).toString();
            String endDate = localDate.toString();

            USGSDownloader downloader = new USGSDownloader();
            Path downloadedPath = downloader.download(startDate, endDate);

            logger.info("Download Path is: "+ downloadedPath);

            Path jsonFilePath = downloadedPath.resolve(Paths.get("output.json"));
            String jsonString = Files.readAllLines(jsonFilePath).get(0);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode featuresNode = rootNode.get("features");

            Path tsvFilePath = downloadedPath.resolve(Paths.get("output.tsv"));
            logger.info("Writing to the CSV file at: "+ tsvFilePath);

            String tsvHeader = "magVal\tplaceVal\ttimeVal\tupdatedVal\ttzVal\turlVal\tdetailVal\tfeltVal\tcdiVal\tmmiVal\t"+
                    "alertVal\tstatusVal\ttsunamiVal\tsigVal\tnetVal\tcodeVal\tidsVal\tsourcesVal\ttypesVal\tnstVal\t"+
                    "dminVal\trmsVal\tgapVal\tmagTypeVal\t+typeVal\tcoordVal0\tcoordVal1\tcoordVal2\n";

            FileWriter writer = new FileWriter(tsvFilePath.toString());
            writer.append(tsvHeader);

            for (JsonNode featureNode : featuresNode) {
                JsonNode propertiesNode = featureNode.get("properties");
                JsonNode geometryNode = featureNode.get("geometry");

                double magVal = propertiesNode.get("mag").asDouble();
                String placeVal = propertiesNode.get("place").asText();
                long timeVal = propertiesNode.get("time").asLong();
                long updatedVal = propertiesNode.get("updated").asLong();
                int tzVal = propertiesNode.get("tz").asInt();
                String urlVal = propertiesNode.get("url").asText();
                String detailVal = propertiesNode.get("detail").asText();
                Integer feltVal = propertiesNode.get("felt").intValue();
                Integer cdiVal = propertiesNode.get("cdi").intValue();
                Integer mmiVal = propertiesNode.get("mmi").intValue();
                String alertVal = propertiesNode.get("alert").asText();
                String statusVal = propertiesNode.get("status").asText();
                int tsunamiVal = propertiesNode.get("tsunami").asInt();
                int sigVal = propertiesNode.get("sig").asInt();
                String netVal = propertiesNode.get("net").asText();
                String codeVal = propertiesNode.get("code").asText();
                String idsVal = propertiesNode.get("ids").asText();
                String sourcesVal = propertiesNode.get("sources").asText();
                String typesVal = propertiesNode.get("types").asText();
                int nstVal = propertiesNode.get("nst").asInt();
                double dminVal = propertiesNode.get("dmin").asDouble();
                double rmsVal = propertiesNode.get("rms").asDouble();
                int gapVal = propertiesNode.get("gap").asInt();
                String magTypeVal = propertiesNode.get("magType").asText();
                String typeVal = propertiesNode.get("type").asText();
                double coordVal0 = geometryNode.get("coordinates").get(0).asDouble();
                double coordVal1 = geometryNode.get("coordinates").get(1).asDouble();
                double coordVal2 = geometryNode.get("coordinates").get(2).asDouble();

                String row = magVal + "\t" + placeVal + "\t" + timeVal + "\t" + updatedVal + "\t" + tzVal + "\t" + urlVal + "\t";
                row += detailVal + "\t" + feltVal + "\t" + cdiVal + "\t" + mmiVal + "\t" + alertVal + "\t" + statusVal + "\t";
                row += tsunamiVal + "\t" + sigVal + "\t" + netVal + "\t" + codeVal + "\t" + idsVal + "\t" + sourcesVal + "\t";
                row += typesVal + "\t" + nstVal + "\t" + dminVal + "\t" + rmsVal + "\t" + gapVal + "\t" + magTypeVal + "\t";
                row += typeVal + "\t" + coordVal0 + "\t" + coordVal1 + "\t" + coordVal2 + "\n";

                writer.append(row);

            }

        } catch (Exception e) {
            logger.error("An error occured: " + e.getMessage());
        }


    }
}
