#!/bin/bash

# Usage: ./send-humidity.sh h1 51

SENSOR_ID=$1
VALUE=$2
PORT=3355

MESSAGE="sensor_id=$SENSOR_ID; value=$VALUE"

echo "📤 Sending humidity message to port $PORT"
echo "📦 Message: $MESSAGE"

echo "$MESSAGE" | nc -u -w1 localhost $PORT
