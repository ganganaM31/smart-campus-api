package com.smartcampus.api.subresources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import com.smartcampus.storage.DataStore;
import com.smartcampus.api.exceptions.SensorUnavailableException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.UUID;

public class SensorReadingResource {
    
    private String sensorId;
    private DataStore dataStore = DataStore.getInstance();
    private Gson gson = new Gson();
    
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        List<SensorReading> readings = dataStore.getReadings(sensorId);
        return Response.ok(gson.toJson(readings)).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(String readingJson) {
        try {
            Sensor sensor = dataStore.getSensor(sensorId);
            
            if (!sensor.getStatus().equals("ACTIVE")) {
                throw new SensorUnavailableException(sensorId);
            }
            
            SensorReading reading = gson.fromJson(readingJson, SensorReading.class);
            if (reading.getId() == null || reading.getId().isEmpty()) {
                reading.setId(UUID.randomUUID().toString());
            }
            if (reading.getTimestamp() == 0) {
                reading.setTimestamp(System.currentTimeMillis());
            }
            
            dataStore.addReading(sensorId, reading);
            sensor.setCurrentValue(reading.getValue());
            
            JsonObject response = new JsonObject();
            response.addProperty("message", "Reading recorded successfully");
            response.addProperty("readingId", reading.getId());
            
            return Response.status(201).entity(response.toString()).build();
        } catch (SensorUnavailableException e) {
            throw e;
        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Invalid reading data");
            return Response.status(400).entity(error.toString()).build();
        }
    }
}