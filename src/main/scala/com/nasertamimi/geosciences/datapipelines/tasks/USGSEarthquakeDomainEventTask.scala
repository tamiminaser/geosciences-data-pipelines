package com.nasertamimi.geosciences.datapipelines.tasks

import com.nasertamimi.geosciences.datapipelines.USGS.earthquake.USGSEarthquakeDownloader
import com.nasertamimi.geosciences.datapipelines.core.{Downloader, Task}
import org.apache.logging.log4j.{LogManager, Logger}

import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter;

object USGSEarthquakeDomainEventTask {

    def main(args: Array[String]): Unit = {
            val logger = LogManager.getLogger(this.getClass)
            logger.info("Start USGS Earthquake Task.")
            logger.info(s"Start Date: ${System.getProperty("start_date")}")
            logger.info(s"End Date: ${System.getProperty("end_date")}")

            val startDate = LocalDate.parse(System.getProperty("start_date"))
            val endDate = LocalDate.parse(System.getProperty("end_date"))

            val downloader: USGSEarthquakeDownloader = new USGSEarthquakeDownloader()

            var runDate = startDate
            var nextDate = runDate.plusDays(1)

            while (runDate.isBefore(endDate) || runDate.isEqual(endDate)) {
                val runDateStr: String = runDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                val nextDateStr: String = nextDate.format(DateTimeFormatter.ISO_LOCAL_DATE)

                logger.info(String.format("Run Date is %s", runDateStr))
                val downloadedPath = downloader.download(runDateStr, nextDateStr)
                logger.info(String.format("Download completed for %s.", runDateStr))
                logger.info(String.format("Download Path is: %s", String.valueOf(downloadedPath)))

                runDate = nextDate
                nextDate = nextDate.plusDays(1)
            }
    }
}
