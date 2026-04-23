package com.smartcampus.api.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.google.gson.JsonObject;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable exception) {
        JsonObject error = new JsonObject();
        error.addProperty("status", 500);
        error.addProperty("error", "Internal Server Error");
        error.addProperty("message", "An unexpected error occurred");
        
        return Response
            .status(500)
            .entity(error.toString())
            .build();
    }
}