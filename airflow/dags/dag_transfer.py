from datetime import datetime
from airflow import DAG
from airflow.models import Variable
from airflow.operators.bash import BashOperator


S3_BUCKET_DAG = Variable.get("S3_BUCKET_DAG")

dag = DAG(
        dag_id="dag_transfer",
        schedule='* * * * *',
        start_date=datetime(2023, 4, 21),
        catchup=False,
)

transfer_dags_from_s3 = BashOperator(
    task_id="run_after_loop",
    bash_command=f"aws s3 sync s3://{S3_BUCKET_DAG}/dags /home/ubuntu/airflow/dags --exact-timestamps",
    dag=dag,
)