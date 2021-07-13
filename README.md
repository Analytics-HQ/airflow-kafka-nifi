# AHQ kafka servers: 

    my-cluster-kafka-bootstrap.kafka:{9091, 9092, 9093}
    my-cluster-zookeeper-client.kafka:2181

1. put airflow-kafka-nifi.py in your dags folder

2. create input topic with two partitions

        bin/kafka-topics.sh --create --zookeeper my-cluster-zookeeper-client.kafka:2181 --replication-factor 1 --partitions 2 --topic  used-car-colors

3. create output topic

        bin/kafka-topics.sh --create --zookeeper my-cluster-zookeeper-client.kafka:2181 --replication-factor 1 --partitions 2 --topic used-car-colors-output

4. launch a Kafka consumer

        bin/kafka-console-consumer.sh --bootstrap-server my-cluster-kafka-bootstrap.kafka:9092 \
            --topic word-count-output \
            --from-beginning \
            --formatter kafka.tools.DefaultMessageFormatter \
            --property print.key=true \
            --property print.value=true \
            --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
            --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

5. then produce data to it

        bin/kafka-console-producer.sh --broker-list my-cluster-kafka-bootstrap.kafka:9092 --topic word-count-input

