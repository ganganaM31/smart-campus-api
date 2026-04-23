package com.smartcampus.storage;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static final DataStore instance = new DataStore();
    
    private Map<String, Room> rooms = new ConcurrentHashMap<>();
    private Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    private DataStore() {
        initializeSampleData();
    }

    public static DataStore getInstance() {
        return instance;
    }

    private void initializeSampleData() {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room room2 = new Room("LAB-101", "Physics Lab", 30);
        
        rooms.put(room1.getId(), room1);
        rooms.put(room2.getId(), room2);
        
        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        sensors.put(sensor1.getId(), sensor1);
        room1.getSensorIds().add(sensor1.getId());
    }

    public Map<String, Room> getAllRooms() {
        return rooms;
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public void deleteRoom(String id) {
        rooms.remove(id);
    }

    public Map<String, Sensor> getAllSensors() {
        return sensors;
    }

    public Sensor getSensor(String id) {
        return sensors.get(id);
    }

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
    }

    public void deleteSensor(String id) {
        sensors.remove(id);
    }

    public List<SensorReading> getReadings(String sensorId) {
        return sensorReadings.getOrDefault(sensorId, new ArrayList<>());
    }

    public void addReading(String sensorId, SensorReading reading) {
        sensorReadings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }
}