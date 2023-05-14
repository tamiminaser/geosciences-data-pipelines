package com.nasertamimi.geosciences.datapipelines;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class USGSEarthquakeDataWriter extends DataWriter {
    private static Logger logger = LogManager.getLogger(USGSEarthquakeDataWriter.class);
    private Path downloadedPath;

    private USGSEarthquakeEvent usgsEarthquakeEvent;

    public USGSEarthquakeDataWriter(Path downloadedPath) throws Exception{
        logger.info("Download Path is: "+ downloadedPath);
        this.downloadedPath = downloadedPath;

        Path jsonFilePath = downloadedPath.resolve(Paths.get("output.json"));
        String jsonString = Files.readAllLines(jsonFilePath).get(0);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);
        usgsEarthquakeEvent = new USGSEarthquakeEvent(rootNode);
    }

    public void write() throws Exception {
        this.write("tsv");
    }
    public void write(String fileType) throws Exception{

        Path tsvFilePath = null;
        String dataToWrite = null;

        if (fileType.equals("tsv")) {
            dataToWrite = usgsEarthquakeEvent.toTsv();
            tsvFilePath = downloadedPath.resolve(Paths.get("output.tsv"));;
        }

        logger.info("Writing to the TSV file at: "+ tsvFilePath);

        assert dataToWrite != null;
        FileWriter writer = new FileWriter(tsvFilePath.toString());
        writer.write(dataToWrite);
        writer.close();
    }
}
