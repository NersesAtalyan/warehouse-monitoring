package com.monitoring.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.monitoring.central.properties")
@SpringBootApplication
public class CentralMonitoringServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CentralMonitoringServiceApplication.class, args);
    }
}
