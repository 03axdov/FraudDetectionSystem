package org.frauddetection.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

public class TransactionProducer {

    private final KafkaProducer<String, Object> producer;

    public TransactionProducer(Properties properties) {
        producer = new KafkaProducer<>(properties);
    }
}
