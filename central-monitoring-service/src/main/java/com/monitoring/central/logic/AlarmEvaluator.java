package com.monitoring.central.logic;

import com.monitoring.central.properties.ThresholdProperties;
import com.monitoring.common.dto.SensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEvaluator {

    private final ThresholdProperties thresholds;

    public void evaluate(SensorData data) {
        switch (data.type()) {
            case SensorData.TEMPERATURE -> {
                if (data.value() > thresholds.temperature()) {
                    log.warn("🚨 TEMP ALERT | {} | Sensor: {} | Value: {}°C",
                            data.warehouseId(), data.sensorId(), data.value());
                }
            }
            case SensorData.HUMIDITY -> {
                if (data.value() > thresholds.humidity()) {
                    log.warn("💧 HUMIDITY ALERT | {} | Sensor: {} | Value: {}%",
                            data.warehouseId(), data.sensorId(), data.value());
                }
            }
            default -> log.warn("⚠️ Unknown sensor type: {}", data.type());
        }
    }
}
