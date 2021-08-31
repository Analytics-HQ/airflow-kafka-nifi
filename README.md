# Airflow-Kafka-Nifi AHQ Demonstration


### BACKGROUND: This project is designed to demonstrate the following integrative features of AHQ: An ETL can be constructed that 
  - gets information from a public API and parses it using python to dump the raw, flat json data into a Kafka topic via an Airflow DAG (`airflow-kafka-nifi.py`)
  - with the input topic filled with the json from the public API in the Kafka topic `used-car-colors`, a Kafka Streams application takes the input and calculates the count of each color by year and outputs the topic to a new output topic in Kafka called `used-car-colors-yearcolor-group`
  - A nifi instance running with a kafka consumer hooked up to the `used-car-colors` and `used-car-colors-yearcolor-group` topic picks up the data and inputs the data to postgres. The nifi flow template is the file `airflow-kafka-nifi-template.xml` -- import this info nifi.

### SETUP: Backend: While the backend needs to be set up in the backend of AHQ, the following would be used in a local setup, and is noted here for assistance in how to set this up on the AHQ backend (within the constraints like strimzi)

1. create input topic with two partitions. 

        bin/kafka-topics.sh --create --zookeeper <your-zookeeper>:2181 --replication-factor 1 --partitions 2 --topic  used-car-colors
        
2. create output topics.  

        bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-output \
        bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-yearcolor-group \ 
        bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-totals-color \
        bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-totals-year
        
3. Every time the developer restartes the kafka stream application, this script will need to be re-run:
      
        bin/kafka-streams-application-reset.sh --application-id used-car-colors-app --input-topics used-car-colors --intermediate-topics used-car-colors-output used-car-colors-totals-year used-car-colors-yearcolor-group --bootstrap-servers <your-kafka-servers>:9092 --zookeeper <your-zookeeper>:2181
        
   see https://www.confluent.io/blog/data-reprocessing-with-kafka-streams-resetting-a-streams-application/


### SETUP: Developer/IDE
  
1. put airflow-kafka-nifi.py in your dags folder

2. launch a Kafka consumer for the output (for observational reasons)

        bin/kafka-console-consumer.sh --bootstrap-server <your-kafka-server>:9092 --topic used-car-colors-yearcolor-group
        
3. launch a Kafka consumer for the unprocessed input

        bin/kafka-console-consumer.sh --bootstrap-server <your-kafka-server>:9092 --topic used-car-colors

4. Java commands to start up the kafka streams project

        mvn clean package
        mvn exec:java -Dexec.mainClass=myapps.UsedCarColorsApp
    

5. then produce data to it

        bin/kafka-console-producer.sh --broker-list a<your-kafka-server>:9092 --topic used-car-colors
        
        


