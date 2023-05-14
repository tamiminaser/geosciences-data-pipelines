package com.nasertamimi.geosciences.datapipelines;

import java.nio.file.Path;

public abstract class Downloader {
    public abstract Path download(String startDate, String endDate) throws Exception;
}
