package com.monitoring.central.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "kafka")
public record KafkaTopicsProperties(List<String> topics) {}
