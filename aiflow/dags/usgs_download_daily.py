from airflow import DAG
from airflow.operators import BashOperator
from datetime import datetime
import os
import sys

args = {
    'owner': 'you',
    'start_date': datetime(2019, 4, 24),
    'provide_context': True
}

dag = DAG(
    task_id='runjar',
    schedule_interval=None,  # manually triggered
    default_args=args)

run_jar_task = BashOperator(
    task_id='runjar',
    dag=dag,
    bash_command='java -Dstart_date=2023-03-25 -Dend_date=2023-03-26 -jar earthquakeAPI-1.0.0-SNAPSHOT-shaded.jar'
)
