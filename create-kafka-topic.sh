#!/bin/bash

TOPIC_NAME="sensor-data"
BROKER_CONTAINER="warehouse_kafka"
BROKER_PORT="warehouse-kafka:9092"
REPLICATION_FACTOR=1
PARTITIONS=1

echo "Creating Kafka topic '$TOPIC_NAME' inside container '$BROKER_CONTAINER'..."

docker exec -i "$BROKER_CONTAINER" /usr/bin/kafka-topics \
  --create \
  --bootstrap-server "$BROKER_PORT" \
  --replication-factor "$REPLICATION_FACTOR" \
  --partitions "$PARTITIONS" \
  --topic "$TOPIC_NAME"

echo "✅ Topic '$TOPIC_NAME' created (if it didn't already exist)."
