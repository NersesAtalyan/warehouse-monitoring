package com.monitoring.central.logic;

import com.monitoring.central.properties.ThresholdProperties;
import com.monitoring.common.dto.SensorData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlarmEvaluatorTest {

    @Test
    void shouldLogTemperatureAlarm_whenValueExceedsThreshold() {
        ThresholdProperties props = new ThresholdProperties(35.0, 50.0);
        AlarmEvaluator evaluator = new AlarmEvaluator(props);

        SensorData data = new SensorData("warehouse-1", "t1", SensorData.TEMPERATURE, 40.0);

        evaluator.evaluate(data);
    }

    @Test
    void shouldNotLogAlarm_whenHumidityBelowThreshold() {
        ThresholdProperties props = new ThresholdProperties(35.0, 50.0);
        AlarmEvaluator evaluator = new AlarmEvaluator(props);

        SensorData data = new SensorData("w1", "h1", SensorData.HUMIDITY, 40.0);
        evaluator.evaluate(data);
    }
}
