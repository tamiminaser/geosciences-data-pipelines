from airflow import DAG
from airflow.models import Variable
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from datetime import datetime

S3_BUCKET = Variable.get("S3_BUCKET")

start_date = '{{dag_run.conf["start_date"]}}'
end_date = '{{dag_run.conf["end_date"]}}'

args = {
    'owner': 'airflow_dummy_owner',
    'start_date': datetime(2023, 1, 1),
    'provide_context': True
}

dag = DAG(
    'earthquake-download-backfill',
    schedule_interval=None,
    default_args=args)

# Tasks

start_task = DummyOperator(task_id='start', dag=dag)
end_task = DummyOperator(task_id='end', dag=dag)


# USGS Earthquake
usgs_earthquake_download_backfill = BashOperator(
    task_id='usgs_earthquake_download_backfill',
    dag=dag,
    bash_command=f"java -Ddownload_type='earthquake' -Dstart_date={start_date} -Dend_date={end_date}  -jar ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar"
)

usgs_earthquake_download_backfill_spark = BashOperator(
    task_id='usgs_earthquake_download_backfill_spark',
    dag=dag,
    bash_command=f""""
        spark-submit 
            --driver-java-options "-Djava.home=/etc/alternatives/jre_11" 
            --class com.nasertamimi.geosciences.datapipelines.tasks.USGSEarthquakeDomainEventTask 
            --master local 
            --conf spark.driver.extraJavaOptions="-Dstart_date={start_date} -Dend_date={end_date}" 
            --conf spark.executor.extraJavaOptions="-Dstart_date={start_date} -Dend_date={end_date}"  
            s3://{S3_BUCKET}/jars/GeosciencesDataPipelines-1.1.1-SNAPSHOT-jar-with-dependencies.jar
    """
)

usgs_earthquake_transfer_data_to_s3 = BashOperator(
    task_id="transfer_data_to_s3",
    bash_command=f"aws s3 sync /tmp/EarthDataLake/earthquakeAPI/source=USGS s3://{S3_BUCKET}/data/EarthDataLake/earthquakeAPI/source=USGS --exact-timestamps",
    dag=dag,
)

# FIRMS Fire
firms_fire_download_backfill = BashOperator(
    task_id='firms_fire_download_backfill',
    dag=dag,
    bash_command=f"java -Ddownload_type='fire' -Dstart_date={start_date} -Dend_date={end_date} -jar ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar"
)

firms_fire_transfer_data_to_s3 = BashOperator(
    task_id="transfer_data_to_s3",
    bash_command=f"aws s3 sync /tmp/EarthDataLake/fireAPI/source=FRIMS s3://{S3_BUCKET}/data/EarthDataLake/fireAPI/source=FRIMS --exact-timestamps",
    dag=dag,
)

# Dependencies
start_task >> usgs_earthquake_download_backfill_spark >> usgs_earthquake_transfer_data_to_s3 >> end_task
start_task >> firms_fire_download_backfill >> firms_fire_transfer_data_to_s3 >> end_task