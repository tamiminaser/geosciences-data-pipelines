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
    schedule_interval='0 * * * *',
    default_args=args)

EXEC_DATE = '{{ execution_date.strftime("%Y-%m-%d") }}' 
PREV_DATE = '{{ (execution_date - macros.timedelta(days=1)).strftime("%Y-%m-%d") }}'
start_date = PREV_DATE
end_date =  EXEC_DATE

###################################
##            TASKS              ##
###################################

start_task = DummyOperator(task_id='start', dag=dag)
end_task = DummyOperator(task_id='end', dag=dag)

usgs_download_daily_task = BashOperator(
    task_id='usgs_download_daily_task',
    dag=dag,
    bash_command=f"java -Dstart_date={start_date} -Dend_date={end_date} -jar ~/airflow/resources/jars/earthquakeAPI-1.0.0-SNAPSHOT-shaded.jar"
)

start_task >> usgs_download_daily_task >> end_task
