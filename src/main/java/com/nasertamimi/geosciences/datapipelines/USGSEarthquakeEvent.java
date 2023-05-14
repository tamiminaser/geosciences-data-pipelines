package com.nasertamimi.geosciences.datapipelines;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Arrays;

public class USGSEarthquakeEvent {

    private JsonNode rootNode;
    private String[] fields = {"mag", "place", "time", "updatedVal", "tz", "url", "detail", "felt", "cdiVal",
            "mmi", "alert", "status", "tsunami", "sig", "net", "code", "ids", "sources", "types", "nst",
            "dmin", "rms", "gap", "magType", "type", "coord0", "coord1", "coord2"};
    public USGSEarthquakeEvent(JsonNode rootNode){
        this.rootNode = rootNode;
    }

    private String headerBuilder(String sep){
        ArrayList<String> fields_ = new ArrayList<String>(Arrays.asList(fields));
        return String.join(sep, fields_)+"\n";
    }

    public String toTsv() {
        return output("\t");
    }

    public String toCsv() {
        return output(",");
    }

    private String output(String sep){
        String header = headerBuilder(sep);
        String dataToWrite = header;

        JsonNode featuresNode = rootNode.get("features");
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
            int feltVal = propertiesNode.get("felt").asInt();
            int cdiVal = propertiesNode.get("cdi").asInt();
            int mmiVal = propertiesNode.get("mmi").asInt();
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

            String dataRow = magVal + sep + placeVal + sep + timeVal + sep + updatedVal + sep +
                    tzVal + sep + urlVal + sep + detailVal + sep + feltVal + sep + cdiVal + sep +
                    mmiVal + sep + alertVal + sep + statusVal + sep + tsunamiVal + sep + sigVal + sep +
                    netVal + sep + codeVal + sep + idsVal + sep + sourcesVal + sep + typesVal + sep +
                    nstVal + sep + dminVal + sep + rmsVal + sep + gapVal + sep + magTypeVal + sep +
                    typeVal + sep + coordVal0 + sep + coordVal1 + sep + coordVal2 + "\n";

            dataToWrite += dataRow;
        }
        return dataToWrite;
    }
}
