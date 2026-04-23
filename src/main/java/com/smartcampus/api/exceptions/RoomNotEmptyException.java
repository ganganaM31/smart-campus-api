package com.smartcampus.api.exceptions;

public class RoomNotEmptyException extends RuntimeException {
    private String roomId;
    
    public RoomNotEmptyException(String roomId) {
        super("Room " + roomId + " still contains active sensors");
        this.roomId = roomId;
    }
    
    public String getRoomId() {
        return roomId;
    }
}