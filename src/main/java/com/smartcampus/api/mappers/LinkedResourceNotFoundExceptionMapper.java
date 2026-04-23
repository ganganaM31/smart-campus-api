package com.smartcampus.api.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.smartcampus.api.exceptions.LinkedResourceNotFoundException;
import com.google.gson.JsonObject;

@Provider
public class LinkedResourceNotFoundExceptionMapper 
        implements ExceptionMapper<LinkedResourceNotFoundException> {
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        JsonObject error = new JsonObject();
        error.addProperty("status", 422);
        error.addProperty("error", "Unprocessable Entity");
        error.addProperty("message", exception.getMessage());
        error.addProperty("resourceType", exception.getResourceType());
        error.addProperty("resourceId", exception.getResourceId());
        
        return Response
            .status(422)
            .entity(error.toString())
            .build();
    }
}