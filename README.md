# Geoscience Data Pipelines

## Introduction

Welcome to the Geoscience Data Pipelines project! This repository offers a comprehensive example of a data pipeline designed for downloading various publicly available geoscience datasets.

In this project, you'll find:

* A Spark Job application
* Airflow DAGs
* CI/CD configurations
* And more!
* 
This template serves as a blueprint for similar jobs that use APIs as their data source. While the project is still under active development and new components are continually being added, it is fully functional in its current state and can be utilized by professionals. 

Feel free to explore and contribute!

## Basics

This project is structured to facilitate continuous integration and continuous deployment (CI/CD) using GitHub Actions, handle data downloading tasks through modular Java code, and orchestrate complex workflows with Apache Airflow. Here's a detailed breakdown of the directory structure and their purposes:

### .github/

In the .github/ directory, you will find configurations related to CI/CD. We leverage GitHub Actions to automate our CI/CD pipelines, ensuring that our codebase is continuously tested, built, and deployed. The workflows defined here help in maintaining code quality and streamline the deployment process, making the development lifecycle efficient and robust.

### src/main/java/

This directory houses all the Java modules responsible for downloading various public datasets. These modules are referred to as "downloaders". Each downloader is designed to handle specific data sources, enabling easy and efficient data acquisition.

#### Key Components:
*Downloaders*: Java classes that manage the process of connecting to, downloading, and processing data from different public datasets.

*Examples*: Located under src/main/com/nasertamimi/geosciences/datapipelines/examples, these are ready-to-run examples that demonstrate how to use the downloaders. By running these examples, you can quickly download sample datasets and understand the functionality of the downloaders.

### src/main/scala/

In the `src/main/scala/` directory, we use Scala to define downloading tasks. These tasks are built on top of the Java downloader classes, providing a higher-level interface for performing data downloads.

#### Current Implementation:

USGS Earthquake Downloader: As of now, we have implemented a task for downloading earthquake data from the United States Geological Survey (USGS). This task serves as a prototype, and we plan to expand this to include more datasets in the future.

### airflow/

The `airflow/` directory contains DAGs (Directed Acyclic Graphs) for Apache Airflow. Airflow is used to orchestrate complex workflows and automate data pipelines.

#### Components:

DAGs: These DAGs define the sequence and dependencies of tasks that need to be executed. They are configured to use the JAR file application, which is uploaded to Amazon S3, to run scheduled jobs. This integration allows for scalable and reliable execution of data pipelines, leveraging Airflow's robust scheduling and monitoring capabilities.