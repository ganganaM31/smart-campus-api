package com.smartcampus.api.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import com.smartcampus.models.Room;
import com.smartcampus.storage.DataStore;
import com.smartcampus.api.exceptions.RoomNotEmptyException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Collection;

@Path("/rooms")
public class RoomResource {
    
    private DataStore dataStore = DataStore.getInstance();
    private Gson gson = new Gson();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        Collection<Room> rooms = dataStore.getAllRooms().values();
        return Response.ok(gson.toJson(rooms)).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(String roomJson) {
        try {
            Room room = gson.fromJson(roomJson, Room.class);
            dataStore.addRoom(room);
            
            JsonObject response = new JsonObject();
            response.addProperty("message", "Room created successfully");
            response.addProperty("id", room.getId());
            
            return Response.status(201).entity(response.toString()).build();
        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Invalid room data");
            return Response.status(400).entity(error.toString()).build();
        }
    }
    
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        if (room == null) {
            return Response.status(404).entity("{\"error\": \"Room not found\"}").build();
        }
        return Response.ok(gson.toJson(room)).build();
    }
    
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        if (room == null) {
            return Response.status(404).entity("{\"error\": \"Room not found\"}").build();
        }
        
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }
        
        dataStore.deleteRoom(roomId);
        
        JsonObject response = new JsonObject();
        response.addProperty("message", "Room deleted successfully");
        
        return Response.ok(response.toString()).build();
    }
}