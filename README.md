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

## Technology Stack
- Java 11+
- JAX-RS (Jakarta)
- Jersey 3.1.1
- Grizzly HTTP Server
- GSON

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

## Features
✓ RESTful resource-based architecture
✓ Sub-resource locators for nested endpoints
✓ Custom exception handling (409, 422, 403, 500)
✓ Request/response logging
✓ Thread-safe in-memory storage
✓ JSON request/response handling

## Error Handling
- **409 Conflict:** Room deletion with active sensors
- **422 Unprocessable Entity:** Invalid foreign key
- **403 Forbidden:** Sensor unavailable
- **500 Internal Error:** Unexpected exceptions

## Answers to Conceptual Questions

### JAX-RS Resource Lifecycle
Resources are instantiated per-request by default. This ensures thread safety. Shared data uses Singleton pattern with ConcurrentHashMap.

### DELETE Idempotency
Yes, DELETE is idempotent. First request deletes (200 OK), subsequent requests return 404 (room doesn't exist). Server state doesn't change on retries.

### Room Listing Implications
Full objects use more bandwidth but better UX. IDs only reduce bandwidth but need extra requests. Our API returns full objects.

### @Consumes Annotation
If client sends wrong Content-Type, JAX-RS returns 415 Unsupported Media Type before handler method executes.

### @QueryParam vs Path Parameter
@QueryParam is better for filtering (supports multiple criteria, standard REST convention). Path parameters represent resource hierarchy.

### Sub-Resource Pattern Benefits
Separates concerns, improves modularity, easier testing, better maintainability, cleaner code organization.

### Exception Mapping Security
Stack traces expose code structure and vulnerabilities. We use GenericExceptionMapper to return generic errors only.

### Filters vs Manual Logging
Filters separate cross-cutting concerns, follow DRY principle, guarantee consistency, no code duplication in every method.
