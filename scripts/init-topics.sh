#!/bin/bash

set -e

echo "Creating Kafka topics..."

/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9093 \
  --create --if-not-exists --topic transactions \
  --partitions 3 --replication-factor 1

echo "Kafka topics created."