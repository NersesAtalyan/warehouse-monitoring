package com.monitoring.common.dto;


public record SensorData(String warehouseId, String sensorId, String type, double value) {
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String HUMIDITY = "HUMIDITY";
}
