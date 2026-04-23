package com.smartcampus.api.exceptions;

public class SensorUnavailableException extends RuntimeException {
    private String sensorId;
    
    public SensorUnavailableException(String sensorId) {
        super("Sensor " + sensorId + " is not available for readings");
        this.sensorId = sensorId;
    }
    
    public String getSensorId() {
        return sensorId;
    }
}