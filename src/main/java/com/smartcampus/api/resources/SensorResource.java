package com.smartcampus.api.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.Room;
import com.smartcampus.storage.DataStore;
import com.smartcampus.api.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.api.subresources.SensorReadingResource;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/sensors")
public class SensorResource {
    
    private DataStore dataStore = DataStore.getInstance();
    private Gson gson = new Gson();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(String sensorJson) {
        try {
            Sensor sensor = gson.fromJson(sensorJson, Sensor.class);
            
            Room room = dataStore.getRoom(sensor.getRoomId());
            if (room == null) {
                throw new LinkedResourceNotFoundException("Room", sensor.getRoomId());
            }
            
            dataStore.addSensor(sensor);
            room.getSensorIds().add(sensor.getId());
            
            JsonObject response = new JsonObject();
            response.addProperty("message", "Sensor created successfully");
            response.addProperty("id", sensor.getId());
            
            return Response.status(201).entity(response.toString()).build();
        } catch (LinkedResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Invalid sensor data");
            return Response.status(400).entity(error.toString()).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        Collection<Sensor> sensors = dataStore.getAllSensors().values();
        
        if (type != null && !type.isEmpty()) {
            sensors = sensors.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        }
        
        return Response.ok(gson.toJson(sensors)).build();
    }
    
    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensor(sensorId);
        if (sensor == null) {
            return Response.status(404).entity("{\"error\": \"Sensor not found\"}").build();
        }
        return Response.ok(gson.toJson(sensor)).build();
    }
    
    @PUT
    @Path("/{sensorId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSensorStatus(@PathParam("sensorId") String sensorId, String body) {

        try {
            Sensor sensor = dataStore.getSensor(sensorId);

            if (sensor == null) {
                return Response.status(404)
                        .entity("{\"error\": \"Sensor not found\"}")
                        .build();
            }

            if (body == null || body.isEmpty()) {
                return Response.status(400)
                        .entity("{\"error\": \"Empty request body\"}")
                        .build();
            }

            JsonObject json = gson.fromJson(body, JsonObject.class);

            if (json == null || !json.has("status")) {
                return Response.status(400)
                        .entity("{\"error\": \"Status field required\"}")
                        .build();
            }

            String newStatus = json.get("status").getAsString();

            sensor.setStatus(newStatus);

            JsonObject response = new JsonObject();
            response.addProperty("message", "Sensor status updated");
            response.addProperty("status", newStatus);

            return Response.ok(response.toString()).build();

        } catch (Exception e) {
            e.printStackTrace(); 

            return Response.status(500)
                    .entity("{\"error\": \"Internal error in update status\"}")
                    .build();
        }
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadings(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensor(sensorId);
        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor", sensorId);
        }
        return new SensorReadingResource(sensorId);
    }
}