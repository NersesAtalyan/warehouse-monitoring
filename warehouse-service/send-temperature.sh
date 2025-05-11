#!/bin/bash

SENSOR_ID=$1
VALUE=$2
PORT=3344

MESSAGE="sensor_id=$SENSOR_ID; value=$VALUE"

echo "📤 Sending temperature message to port $PORT"
echo "📦 Message: $MESSAGE"

echo "$MESSAGE" | nc -u -w1 localhost $PORT
