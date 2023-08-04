package com.nasertamimi.geosciences.datapipelines.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Task {
    protected final Logger logger = LogManager.getLogger(this.getClass());
    public abstract void run();

}
