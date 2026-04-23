# Smart Campus Sensor & Room Management API

**Module:** Client-Server Architectures  
**Coursework:** JAX-RS RESTful Service Development

## Overview
RESTful API for managing campus rooms and IoT sensors using JAX-RS with Jersey.

## API Endpoints

### Discovery
- `GET /api/v1/` - API metadata

### Rooms
- `GET /api/v1/rooms` - Get all rooms
- `POST /api/v1/rooms` - Create room
- `GET /api/v1/rooms/{roomId}` - Get specific room
- `DELETE /api/v1/rooms/{roomId}` - Delete room

### Sensors
- `POST /api/v1/sensors` - Create sensor
- `GET /api/v1/sensors` - Get all sensors
- `GET /api/v1/sensors?type=TYPE` - Filter by type
- `GET /api/v1/sensors/{sensorId}` - Get specific sensor

### Readings
- `GET /api/v1/sensors/{sensorId}/readings` - Get readings
- `POST /api/v1/sensors/{sensorId}/readings` - Add reading

## Build & Run

```bash
cd smart-campus-api
mvn clean install
mvn exec:java -Dexec.mainClass="com.smartcampus.Main"
```

Server runs at: `http://localhost:8080/api/v1/`

## Sample CURL Commands

```bash
# Discovery
curl http://localhost:8080/api/v1/

# Get rooms
curl http://localhost:8080/api/v1/rooms

# Create room
curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LAB-101\",\"name\":\"Physics Lab\",\"capacity\":30}"

# Create sensor
curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-002\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":21.0,\"roomId\":\"LAB-101\"}"

# Filter sensors
curl "http://localhost:8080/api/v1/sensors?type=Temperature"

# Get readings
curl http://localhost:8080/api/v1/sensors/TEMP-001/readings

# Add reading
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d "{\"value\":23.5}"

# Test error (409 Conflict)
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

## Error Handling
- **409 Conflict:** Room deletion with active sensors
- **422 Unprocessable Entity:** Invalid foreign key
- **403 Forbidden:** Sensor unavailable
- **500 Internal Error:** Unexpected exceptions


