package com.monitoring.central.config;

import com.monitoring.central.properties.KafkaTopicsProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(KafkaProperties kafkaProperties,
                                                                                       KafkaTopicsProperties kafkaTopics) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());

        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.<String, String>create(props)
                .subscription(kafkaTopics.topics());

        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }
}
