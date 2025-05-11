package com.monitoring.central.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "thresholds")
public record ThresholdProperties(double temperature, double humidity) {
}
