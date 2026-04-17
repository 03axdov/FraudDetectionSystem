from pyspark.sql import SparkSession
import os


if __name__ == "__main__":
    spark = (
        SparkSession.builder
        .appName("KafkaDebug")
        .getOrCreate()
    )
    
    spark.sparkContext.setLogLevel("WARN")

    df = (
        spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", os.getenv("KAFKA_BOOTSTRAP_SERVERS"))
        .option("subscribe", "transactions")
        .option("startingOffsets", "earliest")
        .load()
    )

    # Print raw data (key + value are binary)
    query = (
        df.writeStream
        .format("console")
        .option("truncate", False)
        .start()
    )

    query.awaitTermination()