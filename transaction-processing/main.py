from dotenv import load_dotenv
import os
from kafka.TransactionConsumer import TransactionConsumer
from threading import Event, Thread
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer

TOPICS = ["transactions"]

if __name__ == "__main__":
    
    load_dotenv()
    
    conf = {
        "bootstrap.servers": os.getenv("KAFKA_BOOTSTRAP_SERVERS"),
        "group.id": "fraud-detectors",
        "auto.offset.reset": "earliest"
    }
    
    schema_registry_conf = {
        "url": os.getenv("SCHEMA_REGISTRY_URL")
    }
    schema_registry_client = SchemaRegistryClient(schema_registry_conf)
    avro_deserializer = AvroDeserializer(
        schema_registry_client
    )
    
    stop_event = Event()
    transaction_consumer = TransactionConsumer(conf, TOPICS, stop_event, avro_deserializer)
    
    transaction_thread = Thread(target=transaction_consumer.start, args=())

    transaction_thread.start()
    
    try:
        transaction_thread.join()
    except KeyboardInterrupt:
        print("Interrupting consumers...")
        stop_event.set()
        
        transaction_thread.join()
        print("Consumers stopped.")