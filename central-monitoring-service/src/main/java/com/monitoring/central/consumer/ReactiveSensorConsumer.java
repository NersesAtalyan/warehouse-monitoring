package com.monitoring.central.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.central.logic.AlarmEvaluator;
import com.monitoring.common.dto.SensorData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveSensorConsumer {

    private final ReactiveKafkaConsumerTemplate<String, String> kafka;
    private final ObjectMapper objectMapper;
    private final AlarmEvaluator alarmEvaluator;

    @PostConstruct
    public void listen() {
        kafka.receive()
                .flatMap(record ->
                        toSensorData(record.value())
                                .doOnNext(alarmEvaluator::evaluate)
                                .doOnNext(sd -> log.info("✅ Processed: {}", sd))
                                .then(Mono.fromRunnable(record.receiverOffset()::acknowledge))
                                .onErrorResume(ex -> {
                                    log.error("💥 Failed to process record: {}", record.value(), ex);
                                    return Mono.empty();
                                })
                )
                .subscribe();
    }

    private Mono<SensorData> toSensorData(String json) {
        return Mono.fromCallable(() -> objectMapper.readValue(json, SensorData.class))
                .subscribeOn(Schedulers.boundedElastic());
    }
}

