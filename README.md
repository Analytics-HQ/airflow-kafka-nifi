# Airflow-Kafka-Nifi AHQ Demonstration

### This project is designed to demonstrate the following integrative features of AHQ: An ETL can be constructed that 
  - gets information from a public API and parses it using python to dump the raw, flat json data into a Kafka topic via an Airflow DAG (`airflow-kafka-nifi.py`)
  - with the input topic filled with the json from the public API in the Kafka topic `used-car-colors`, a Kafka Streams application takes the input and calculates the count of each color by year and outputs the topic to a new output topic in Kafka called `used-car-colors-output`
  - A nifi instance running with a kafka consumer hooked up to the `used-car-colors-output` topic picks up the data and inputs the data to postgres.

  

1. put airflow-kafka-nifi.py in your dags folder

2. create input topic with two partitions. Note: this has changed and Kevin will have to create the topic via strimzi.

        bin/kafka-topics.sh --create --zookeeper my-cluster-zookeeper-client.kafka:2181 --replication-factor 1 --partitions 2 --topic  used-car-colors-2

3. create output topic.  Note: this has changed and Kevin will have to create the topic via strimzi.

        bin/kafka-topics.sh --create --zookeeper my-cluster-zookeeper-client.kafka:2181 --replication-factor 1 --partitions 2 --topic used-car-colors-output

4. launch a Kafka consumer for the output

        bin/kafka-console-consumer.sh --bootstrap-server app-kafka-cluster-kafka-brokers:9092 --topic used-car-colors-output
        
5. launch a Kafka consumer for the unprocessed input

        bin/kafka-console-consumer.sh --bootstrap-server app-kafka-cluster-kafka-brokers:9092 --topic used-car-colors-2

6. Java commands to start up the kafka streams project

        mvn clean package
        mvn exec:java -Dexec.mainClass=KafkaStreamsExample
    

6. then produce data to it

        bin/kafka-console-producer.sh --broker-list app-kafka-cluster-kafka-brokers:9092 --topic used-car-colors-2
        
        
7. To reset the kafka streams app:
      
        bin/kafka-streams-application-reset.sh --application-id used-car-colors-app --input-topics used-car-colors --intermediate-topics used-car-colors-output --bootstrap-servers localhost:9092 --zookeeper localhost:2181
        
        see https://www.confluent.io/blog/data-reprocessing-with-kafka-streams-resetting-a-streams-application/

