/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.StringTokenizer;

public class KafkaStreamsConfig {
    private static final Logger log = LogManager.getLogger(KafkaStreamsConfig.class);

    private static final int DEFAULT_COMMIT_INTERVAL_MS = 5000;
    private final String bootstrapServers;
    private final String applicationId;
    private final String sourceTopic;
    private final String targetTopic;


    public KafkaStreamsConfig(String bootstrapServers, String applicationId, String sourceTopic, String targetTopic) {
        this.bootstrapServers = bootstrapServers;
        this.applicationId = applicationId;
        this.sourceTopic = sourceTopic;
        this.targetTopic = targetTopic;

    }

    public static KafkaStreamsConfig fromEnv() {
        String bootstrapServers = "app-kafka-cluster-kafka-brokers:9092";
        String sourceTopic = "used-car-colors-2";
        String targetTopic = "used-car-colors-2";
        String applicationId = "used-car-colors-2";

        return new KafkaStreamsConfig(bootstrapServers, applicationId, sourceTopic, targetTopic);
    }

    public static Properties createProperties(KafkaStreamsConfig config) {
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, config.getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, DEFAULT_COMMIT_INTERVAL_MS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        return props;
    }
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getSourceTopic() {
        return sourceTopic;
    }

    public String getTargetTopic() {
        return targetTopic;
    }


    @Override
    public String toString() {
        return "KafkaStreamsConfig{" +
            "bootstrapServers='" + bootstrapServers + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", sourceTopic='" + sourceTopic + '\'' +
            ", targetTopic='" + targetTopic + '\'' +

            '}';
    }
}
