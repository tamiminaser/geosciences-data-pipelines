from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from airflow.macros import  ds_add
from datetime import datetime
import os
import sys

args = {
    'owner': 'airflow_dummy_owner',
    'start_date': datetime(2019, 4, 24),
    'provide_context': True
}

dag = DAG(
    'earthquakeAPI',
    schedule_interval=None,  # manually triggered
    default_args=args)

EXEC_DATE = '{{ ds }}'
start_date = ds_add(EXEC_DATE, -1)
end_date = EXEC_DATE

###################################
##            TASKS              ##
###################################

start_task = DummyOperator(task_id='start', dag=dag)
end_task = DummyOperator(task_id='end', dag=dag)

usgs_download_daily_task = BashOperator(
    task_id='usgs_download_daily_task',
    dag=dag,
    bash_command=f"java -Dstart_date={start_date} -Dend_date={end_date} -jar s3://naser-tamimi-test1/jars/earthquakeAPI-1.0.0-SNAPSHOT-shaded.jar"
)

start_task >> usgs_download_daily_task >> end_task
