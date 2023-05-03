from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.operators.dummy_operator import DummyOperator
from datetime import datetime

args = {
    'owner': 'airflow_dummy_owner',
    'start_date': datetime(2019, 4, 24),
    'provide_context': True
}

dag = DAG(
    'earthquakeAPI-Daily',
    schedule_interval=None,
    default_args=args)

EXEC_DATE = '{{ execution_date.strftime("%Y-%m-%d") }}' 
PREV_DATE = '{{ (execution_date - macros.timedelta(days=1)).strftime("%Y-%m-%d") }}'
start_date = PREV_DATE
end_date =  EXEC_DATE

# Tasks

start_task = DummyOperator(task_id='start', dag=dag)
end_task = DummyOperator(task_id='end', dag=dag)

usgs_download_daily_task = BashOperator(
    task_id='usgs_download_daily_task',
    dag=dag,
    bash_command=f"java -Dstart_date={start_date} -Dend_date={end_date} -jar ~/airflow/resources/jars/earthquakeAPI-1.0.1-SNAPSHOT-shaded.jar"
)

# Dependencies
start_task >> usgs_download_daily_task >> end_task
