from datetime import datetime
from airflow import DAG
from airflow.providers.amazon.aws.operators.s3 import S3FileTransformOperator
from airflow.models import Variable

S3_BUCKET_DAG = Variable.get['S3_BUCKET_DAG']

dag = DAG(
        dag_id="dag_transfer",
        schedule='0',
        start_date=datetime(2023, 4, 21),
        catchup=False,
)

transfer_dags_from_s3 = S3FileTransformOperator(
    task_id="transfer_dags_from_s3",
    source_s3_key=f"s3://{S3_BUCKET_DAG}/airflow/dags",
    dest_s3_key="/home/ubuntu/airflow/dags",
    transform_script="/bin/cp",
    dag=dag
)