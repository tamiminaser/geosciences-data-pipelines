from airflow import DAG
from airflow.models import Variable
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from datetime import datetime

S3_BUCKET = Variable.get("S3_BUCKET")

PREV_DATE = '{{ (execution_date - macros.timedelta(days=1)).strftime("%Y-%m-%d") }}'
run_date = PREV_DATE  #Remember: we alway get data for the previous day.


args = {
    'owner': 'airflow_owner',
    'start_date': datetime(2023, 1, 1),
    'provide_context': True
}

dag = DAG(
    'earthquake-download-daily',
    schedule_interval="0 0 * * *",
    default_args=args)

# Tasks

start_task = DummyOperator(task_id='start', dag=dag)
end_task = DummyOperator(task_id='end', dag=dag)

# USGS Earthquake
usgs_earthquake_download_daily = BashOperator(
    task_id='usgs_earthquake_download_daily',
    dag=dag,
    bash_command=f"java -Dstart_date={run_date} -Dend_date={run_date} -cp ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar com.nasertamimi.geosciences.datapipelines.tasks.USGSEarthquakeDomainEventTask"
)

usgs_earthquake_download_daily_spark = BashOperator(
    task_id='usgs_earthquake_download_daily_spark',
    dag=dag,
    bash_command=f""""
        spark-submit 
            --driver-java-options "-Djava.home=/etc/alternatives/jre_11" 
            --class com.nasertamimi.geosciences.datapipelines.tasks.USGSEarthquakeDomainEventTask 
            --master local 
            --conf spark.driver.extraJavaOptions="-Dstart_date={run_date} -Dend_date={run_date}" 
            --conf spark.executor.extraJavaOptions="-Dstart_date={run_date} -Dend_date={run_date}"  
            s3://{S3_BUCKET}/jars/GeosciencesDataPipelines-1.1.1-SNAPSHOT-jar-with-dependencies.jar
    """
)


# Dependencies
start_task >>  usgs_earthquake_download_daily_spark >> end_task
