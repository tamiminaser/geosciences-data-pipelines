from airflow import DAG
from airflow.models import Variable
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from datetime import datetime

S3_BUCKET_DAG = Variable.get("S3_BUCKET_DAG")
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
    bash_command=f"java -Ddownload_type='earthquake' -Dstart_date={run_date} -Dend_date={run_date} -jar ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar"
)

usgs_earthquake_transfer_data_to_s3 = BashOperator(
    task_id="transfer_data_to_s3",
    bash_command=f"aws s3 sync /tmp/EarthDataLake/earthquakeAPI/source=USGS s3://{S3_BUCKET_DAG}/data/EarthDataLake/earthquakeAPI/source=USGS --exact-timestamps",
    dag=dag,
)

# FIRMS Fire
firms_fire_download_daily = BashOperator(
    task_id='firms_fire_download_daily',
    dag=dag,
    bash_command=f"java -Ddownload_type='fire' -Dstart_date={run_date} -Dend_date={run_date} -jar ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar"
)

firms_fire_transfer_data_to_s3 = BashOperator(
    task_id="transfer_data_to_s3",
    bash_command=f"aws s3 sync /tmp/EarthDataLake/fireAPI/source=FRIMS s3://{S3_BUCKET_DAG}/data/EarthDataLake/fireAPI/source=FRIMS --exact-timestamps",
    dag=dag,
)

# Dependencies
start_task >> usgs_earthquake_download_daily >> usgs_earthquake_transfer_data_to_s3 >> end_task
start_task >> firms_fire_download_daily >> firms_fire_transfer_data_to_s3 >> end_task