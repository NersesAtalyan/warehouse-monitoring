package com.monitoring.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.monitoring.warehouse.properties")
@SpringBootApplication
public class WarehouseApplication {
    public static void main(String[] args) {

        SpringApplication.run(WarehouseApplication.class, args);
    }
}