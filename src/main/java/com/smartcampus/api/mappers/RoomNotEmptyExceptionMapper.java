package com.smartcampus.api.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.smartcampus.api.exceptions.RoomNotEmptyException;
import com.google.gson.JsonObject;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        JsonObject error = new JsonObject();
        error.addProperty("status", 409);
        error.addProperty("error", "Conflict");
        error.addProperty("message", exception.getMessage());
        error.addProperty("roomId", exception.getRoomId());
        
        return Response
            .status(409)
            .entity(error.toString())
            .build();
    }
}