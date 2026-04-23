package com.smartcampus.api.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.smartcampus.api.exceptions.SensorUnavailableException;
import com.google.gson.JsonObject;

@Provider
public class SensorUnavailableExceptionMapper 
        implements ExceptionMapper<SensorUnavailableException> {
    
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        JsonObject error = new JsonObject();
        error.addProperty("status", 403);
        error.addProperty("error", "Forbidden");
        error.addProperty("message", exception.getMessage());
        error.addProperty("sensorId", exception.getSensorId());
        
        return Response
            .status(403)
            .entity(error.toString())
            .build();
    }
}