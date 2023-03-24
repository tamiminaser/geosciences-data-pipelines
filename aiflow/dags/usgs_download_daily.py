from airflow import DAG
from airflow.operators import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from datetime import datetime
import os
import sys

args = {
    'owner': 'airflow_dummy_owner',
    'start_date': datetime(2019, 4, 24),
    'provide_context': True
}

dag = DAG(
    dag_name='earthquakeAPI',
    schedule_interval=None,  # manually triggered
    default_args=args)

EXEC_DATE = '{{ ds }}'

start_task = DummyOperator(task_id='start', dag=dag)
end_task = DummyOperator(task_id='end', dag=dag)


usgs_download_daily_task = BashOperator(
    task_id='usgs_download_daily_task',
    dag=dag,
    bash_command='java -Dstart_date=2023-03-25 -Dend_date=2023-03-26 -jar earthquakeAPI-1.0.0-SNAPSHOT-shaded.jar'
)


start_task >> usgs_download_daily_task >> end_task