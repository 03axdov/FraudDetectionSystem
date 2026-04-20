import os
import requests

from pyspark.sql import SparkSession
from pyspark.sql.functions import col, expr
from pyspark.sql.avro.functions import from_avro

SCHEMA_REGISTRY_URL = os.getenv("SCHEMA_REGISTRY_URL")
TOPIC = "transactions"
SUBJECT = f"{TOPIC}-value"

def get_latest_schema(subject: str) -> str:
    url = f"{SCHEMA_REGISTRY_URL}/subjects/{subject}/versions/latest"
    resp = requests.get(url, timeout=10)
    resp.raise_for_status()
    return resp.json()["schema"]

if __name__ == "__main__":
    avro_schema_json = get_latest_schema(SUBJECT)

    spark = (
        SparkSession.builder
        .appName("KafkaAvroDebug")
        .getOrCreate()
    )
    spark.sparkContext.setLogLevel("WARN")

    df = (
        spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", os.getenv("KAFKA_BOOTSTRAP_SERVERS"))
        .option("subscribe", TOPIC)
        .option("startingOffsets", "earliest")
        .load()
    )

    # Confluent wire format:
    # byte 0      = magic byte
    # bytes 1..4  = schema id
    # bytes 5..   = actual Avro payload
    decoded = (
        df.select(
            col("topic"),
            col("partition"),
            col("offset"),
            col("timestamp"),
            expr("substring(value, 6, length(value)-5)").alias("avro_payload")
        )
        .select(
            "topic",
            "partition",
            "offset",
            "timestamp",
            from_avro(col("avro_payload"), avro_schema_json).alias("data")
        )
        .select("topic", "partition", "offset", "timestamp", "data.*")
    )

    query = (
        decoded.writeStream
        .format("console")
        .option("truncate", False)
        .start()
    )

    query.awaitTermination()