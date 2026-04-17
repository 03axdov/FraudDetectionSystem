from confluent_kafka import Consumer, KafkaException
from threading import Event
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import SerializationContext, MessageField

class TransactionConsumer:
    def __init__(
        self, 
        conf: dict[str, object], 
        topics: list[str], 
        stop_event: Event,
        avro_deserializer: AvroDeserializer
    ):
        self.consumer = Consumer(conf)
        self.topics = topics
        self.stop_event = stop_event
        self.avro_deserializer = avro_deserializer
                
    def start(self):
        try:
            print(f"Listening to: {self.topics}")
            self.consumer.subscribe(self.topics)
            
            while not self.stop_event.is_set():
                msg = self.consumer.poll(1.0)
                if msg is None: continue
                
                if msg.error():
                    raise KafkaException(msg.error())
                
                print(f"Consumed: {self.avro_deserializer(msg.value(), SerializationContext(msg.topic(), MessageField.VALUE))}")
                
        finally:
            self.shutdown()
    
    def shutdown(self):
        self.consumer.close()