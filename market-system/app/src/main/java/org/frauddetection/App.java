package org.frauddetection;

import org.neo4j.driver.GraphDatabase;

import java.util.Properties;

import org.frauddetection.simulator.MarketSimulator;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class App {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver(
            System.getenv("NEO4J_URI"),
            AuthTokens.basic(
                System.getenv("NEO4J_USERNAME"),
                System.getenv("NEO4J_PASSWORD")
            )
        );

        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        kafkaProperties.put("schema.registry.url", System.getenv("SCHEMA_REGISTRY_URL"));

        try {
            MarketSimulator marketSimulator = new MarketSimulator(driver);
            marketSimulator.simulateMarket(25, 3, 200);
        } finally {
            driver.close();
        }
        
    }
}
