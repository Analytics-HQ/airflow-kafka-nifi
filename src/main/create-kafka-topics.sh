bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors \
bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-output \
bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-yearcolor-group \ 
bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-totals-color \
bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 2  --topic used-car-colors-totals-year