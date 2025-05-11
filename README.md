# 📦 Warehouse Monitoring System

This is a reactive system for monitoring temperature and humidity sensors in warehouses. It consists of two main components:

- **Warehouse Service** (UDP Listener + Kafka Producer)
- **Central Monitoring Service** (Kafka Consumer + Alarm Evaluator)

---

## 🧩 System Overview

- Sensors send data via **UDP** to the Warehouse Service.
- Warehouse Service parses and publishes sensor data to Kafka.
- Central Monitoring Service consumes messages from Kafka, evaluates thresholds, and logs alarms if exceeded.

---

## 🧪 Sensor Data Format

| Sensor Type | UDP Port | Message Format               | Threshold |
|-------------|----------|------------------------------|-----------|
| Temperature | 3344     | `sensor_id=t1; value=30`     | 35°C      |
| Humidity    | 3355     | `sensor_id=h1; value=40`     | 50%       |

---

## 🧰 Tech Stack

- Java 21
- Gradle
- Spring Boot 3.x
- Reactor Kafka
- Project Reactor
- Testcontainers
- Docker & Docker Compose
- Lombok
- JUnit 5

---

## 📁 Project Structure

