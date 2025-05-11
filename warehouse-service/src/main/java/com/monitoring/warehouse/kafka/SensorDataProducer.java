package com.monitoring.warehouse.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.common.dto.SensorData;
import com.monitoring.warehouse.properties.WarehouseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorDataProducer {

    private final KafkaSender<String, String> kafkaSender;
    private final WarehouseProperties properties;
    private final ObjectMapper objectMapper;

    public void publish(SensorData data) {
        String topic = switch (data.type()) {
            case SensorData.TEMPERATURE -> properties.temperatureTopic();
            case SensorData.HUMIDITY -> properties.humidityTopic();
            default -> throw new IllegalArgumentException("Unknown sensor type: " + data.type());
        };

        try {
            String key = data.sensorId();
            String value = objectMapper.writeValueAsString(data); // Serialize here

            kafkaSender.send(
                            Flux.just(SenderRecord.create(new ProducerRecord<>(topic, key, value), null))
                    )
                    .doOnError(err -> log.error("Failed to send to Kafka", err))
                    .subscribe();

        } catch (Exception e) {
            log.error("Failed to serialize SensorData", e);
        }
    }
}
