from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from airflow.macros import  ds_add
from datetime import datetime
import os
import sys
from airflow.utils.task_group import TaskGroup



args = {
    'owner': 'airflow_dummy_owner',
    'start_date': datetime(2019, 4, 24),
    'provide_context': True
}

dag = DAG(
    'dummyDag2',
    schedule_interval=None,  # manually triggered
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

#task = {}
#with TaskGroup(group_id="dq_tasks", dag=dag) as dq_tasks:
#    for i in range(10):
#        task[i] = DummyOperator(task_id=f"task-{i}", dag=dag)

dq_tasks = []
for i in range(10):
    dq_tasks.append(DummyOperator(task_id=f"dq-task-{i}", dag=dag))


start_task >> usgs_download_daily_task >> dq_tasks >> end_task