// WarehouseProperties.java
package com.monitoring.warehouse.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "warehouse")
public record WarehouseProperties(String id,
                                  Udp udp,
                                  String temperatureTopic,
                                  String humidityTopic) {
    public record Udp(int temperature, int humidity) {}
}
