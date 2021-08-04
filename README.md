# AHQ kafka servers: 

    my-cluster-kafka-bootstrap.kafka:{9091, 9092, 9093}
    my-cluster-zookeeper-client.kafka:2181

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

