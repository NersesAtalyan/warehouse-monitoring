package com.monitoring.central;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.time.Duration;

import com.monitoring.central.logic.AlarmEvaluator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class SensorDataConsumerIT {

    @Container
    @ServiceConnection          // Boot ≥3.1
    static final KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"))
                    ;   // աշխատում Testcontainers ≥1.20

    @Autowired
    ReactiveKafkaProducerTemplate<String, String> producer;

    @MockitoBean
    AlarmEvaluator alarmEvaluator;

    @Test
    void shouldRaiseAlarmWhenTemperatureExceedsThreshold() {
        var payload = """
                {"warehouseId":"w1","sensorId":"t1","type":"TEMPERATURE","value":42.0}
                """;

        producer.send("temperature-measurements", payload).block();

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() ->
                        verify(alarmEvaluator).evaluate(
                                argThat(sd -> "t1".equals(sd.sensorId()) && sd.value() == 42.0)
                        ));
    }
}

