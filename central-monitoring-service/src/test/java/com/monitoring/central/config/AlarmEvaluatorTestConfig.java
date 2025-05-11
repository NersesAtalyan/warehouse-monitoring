package com.monitoring.central.config;

import com.monitoring.central.logic.AlarmEvaluator;
import com.monitoring.central.properties.ThresholdProperties;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AlarmEvaluatorTestConfig {

    @Bean
    public AlarmEvaluator alarmEvaluator() {
        return Mockito.spy(new AlarmEvaluator(new ThresholdProperties(35.0, 50.0)));
    }
}
