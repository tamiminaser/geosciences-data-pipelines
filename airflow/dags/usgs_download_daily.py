from airflow import DAG
from airflow.models import Variable
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from datetime import datetime

S3_BUCKET_DAG = Variable.get("S3_BUCKET_DAG")
EXEC_DATE = '{{ execution_date.strftime("%Y-%m-%d") }}'
PREV_DATE = '{{ (execution_date - macros.timedelta(days=1)).strftime("%Y-%m-%d") }}'
start_date = PREV_DATE
end_date = EXEC_DATE


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

usgs_download_daily_task = BashOperator(
    task_id='usgs_download_daily_task',
    dag=dag,
    bash_command=f"java -Dstart_date={start_date} -Dend_date={end_date} -jar ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar"
)

transfer_data_to_s3 = BashOperator(
    task_id="transfer_data_to_s3",
    bash_command=f"aws s3 sync /tmp/earthquakeAPI/source=USGS s3://{S3_BUCKET_DAG}/data/earthquakeAPI/source=USGS --exact-timestamps",
    dag=dag,
)

# Dependencies
start_task >> usgs_download_daily_task >> transfer_data_to_s3 >> end_task
