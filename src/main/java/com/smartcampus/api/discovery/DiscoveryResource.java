package com.smartcampus.api.discovery;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.gson.JsonObject;

@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {
        JsonObject discovery = new JsonObject();
        discovery.addProperty("version", "1.0.0");
        discovery.addProperty("title", "Smart Campus Sensor & Room Management API");
        discovery.addProperty("contact", "facilities@university.edu");
        
        JsonObject resources = new JsonObject();
        resources.addProperty("rooms", "/api/v1/rooms");
        resources.addProperty("sensors", "/api/v1/sensors");
        
        discovery.add("resources", resources);
        
        return Response.ok(discovery.toString()).build();
    }
}