from airflow import DAG
from airflow.sensors.filesystem import FileSensor
from airflow.operators.python import PythonOperator
from airflow.operators.bash import BashOperator
from kafka import KafkaProducer
#from airflow.providers.http.operators.http import SimpleHttpOperator
#from airflow.providers.http.operators.http import SimpleHttpOperator

from datetime import datetime, timedelta
import csv
import requests
import json

default_args = {
    "owner": "airflow",
    "email_on_failure": False,
    "email_on_retry": False,
    "email": "admin@localhost.com",
    "retries": 1,
    "retry_delay": timedelta(minutes=5)
}


def download_car_data_api():
    print("!!! [AHQ] [AHQ] [AHQ] [AHQ] [AHQ] [AHQ] [AHQ] !!!")
    # BASE_URL = "https://my.api.mockaroo.com"
    # ENDPOINT = "/used_car_colors.json?key="
    # KEY = "7e18d430"
    indata = requests.get(
        'https://my.api.mockaroo.com/used_car_colors.json?key=7e18d430')

    used_car_colors_data = json.loads(indata.text)

    # kafka
    producer = KafkaProducer(
        bootstrap_servers='my-cluster-kafka-bootstrap.kafka:9092')
    for dict_item in used_car_colors_data:
        # take the row (dict item) and convert back to string and then binary the put in kafka topic
        producer.send('used-car-colors', json.dumps(dict_item).encode())
        # for now just print in airflow until we get kafka working (one thing at a time!)
        print(dict_item)


with DAG("airflow-kafka-nifi", start_date=datetime(2021, 1, 1),
         schedule_interval="@daily", default_args=default_args, catchup=False) as dag:

    car_data_from_api = PythonOperator(

        task_id="download_car_data_api",
        python_callable=download_car_data_api
    )
