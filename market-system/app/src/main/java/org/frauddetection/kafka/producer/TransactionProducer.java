package org.frauddetection.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.common.serialization.StringSerializer;
import org.frauddetection.mappers.TransactionAvroMapper;
import org.frauddetection.models.events.Transaction;
import org.frauddetection.avro.AvroTransaction;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class TransactionProducer {

    private final KafkaProducer<String, Object> producer;

    private static Properties defaultProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", System.getenv("SCHEMA_REGISTRY_URL"));

        return props;
    }

    public TransactionProducer() {
        this(TransactionProducer.defaultProperties());
    }

    public TransactionProducer(Properties properties) {
        producer = new KafkaProducer<>(properties);
    }

    public void send(Transaction transaction, String topic) {
        AvroTransaction avroTransaction = TransactionAvroMapper.toAvro(transaction);
        
        producer.send(new ProducerRecord<>(topic, transaction.fromAccountId(), avroTransaction));
    }
}
