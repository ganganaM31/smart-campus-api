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

# Questions

## Part 1: Service Architecture & Setup
 
### Question 1: JAX-RS Resource Lifecycle
 
In JAX-RS, by default, a new instance of a resource class is created for every incoming HTTP request. This is known as a per-request lifecycle.
 
This design ensures thread safety because each request operates on a separate object instance, avoiding shared mutable state issues. However, since the resource objects are not shared, any application data must be stored in shared structures such as a static HashMap or a singleton class.
 
In this implementation, a singleton DataStore is used to maintain application state across requests. This prevents data loss and ensures consistency. Additionally, since multiple requests can access shared data simultaneously, proper synchronisation or careful design is required to avoid race conditions.
 
### Question 2: HATEOAS and Hypermedia
 
HATEOAS (Hypermedia as the Engine of Application State) is a key constraint of RESTful architecture where responses include links that guide clients on available actions.
 
Providing hypermedia links allows clients to dynamically navigate the API without hardcoding endpoints. This improves flexibility and reduces coupling between client and server.
 
Compared to static documentation, HATEOAS enables:
 
- Better API discoverability
- Reduced dependency on external docs
- Easier evolution of APIs without breaking clients
Also, it improves the usability and scalability of RESTful systems.
 
---
 
## Part 2: Room Management
 
### Question 3: Room Listing Implications
 
Returning only IDs reduces network bandwidth usage, making responses lightweight and efficient, especially for large datasets. However, it increases the need for additional client requests to retrieve full details.
 
Returning full objects improves client usability, as all required information is available in a single response, reducing extra API calls.
 
In this system, returning full objects is more suitable because:
 
- The dataset is relatively small
- It simplifies client-side processing
### Question 4: DELETE Idempotency
 
YES, the DELETE operation is fully idempotent in our implementation. An operation is idempotent if calling it multiple times produces the same result as calling it once.
 
**What Happens on Multiple DELETE Requests:**
 
**First Request:** `DELETE /api/v1/rooms/LIB-301`
- Room exists, deleted, Returns 200 OK
- Server state: Room is gone
**Second Request:** `DELETE /api/v1/rooms/LIB-301`
- Room doesn't exist, returns 404 Not Found
- Server state: Still no room (unchanged)
**Third Request:** `DELETE /api/v1/rooms/LIB-301`
- Same result: 404 Not Found
- Server state: Unchanged
---
 
## Part 3: Sensor Operations
 
### Question 5: @Consumes Annotation
 
The `@Consumes(MediaType.APPLICATION_JSON)` annotation tells JAX-RS to only accept JSON requests. If a client sends a different format, JAX-RS rejects the request BEFORE the handler method executes.
 
**Example Scenario:**
 
**Correct Request:**
```
POST /api/v1/sensors
Content-Type: application/json
{"id":"TEMP-001","type":"Temperature"}
```
Accepted and Handler method executes
 
**Wrong Format Request:**
```
POST /api/v1/sensors
Content-Type: text/plain
{"id":"TEMP-001","type":"Temperature"}
```
Rejected by the framework
 
**JAX-RS Response:**
```
HTTP/1.1 415 Unsupported Media Type
The server refused this request because the request entity is in a 
format not supported by the requested resource for this method.
```
 
### Question 6: QueryParam vs PathParam for Filtering
 
Using `@QueryParam` (e.g., `/sensors?type=CO2`) is more appropriate for filtering because:
 
- It represents optional filtering criteria
- It keeps the resource path clean and consistent
- It allows combining multiple filters easily
In contrast, using path parameters (e.g., `/sensors/type/CO2`):
 
- Implies a fixed resource hierarchy
- Reduces flexibility
- Makes API less scalable
So, the query parameters are better suited for search and filtering operations.
 
---
 
## Part 4: Deep Nesting with Sub Resources
 
### Question 7: Sub-Resource Locator Benefits
 
The Sub-Resource Locator pattern allows delegation of nested resource handling to separate classes.
 
Benefits include:
 
- Improved code organisation
- Separation of concerns
- Better scalability for large APIs
- Easier maintenance and readability
Instead of placing all logic in one large controller, each resource manages its own functionality, making the system modular and easier to extend.
 
---
 
## Part 5: Advanced Error Handling & Logging
 
### Question 8: HTTP 422 vs 404
 
Returning only IDs reduces network bandwidth usage, making responses lightweight and efficient, especially for large datasets. However, it increases the need for additional client requests to retrieve full details.
 
Returning full objects improves client usability, as all required information is available in a single response, reducing extra API calls.
 
In this system, returning full objects is more suitable because:
 
- The dataset is relatively small
- It simplifies client-side processing
### Question 9: Stack Trace Security Risks
 
Exposing internal Java stack traces can lead to serious security risks:
 
- Reveals internal class structure
- Exposes package names and system architecture
- Provides clues about vulnerabilities
- Helps attackers craft targeted exploits
Also, a global exception handler is used to return generic error messages, preventing sensitive information leakage.
 
### Question 10: JAX-RS Filters vs Manual Logging
 
Using JAX-RS filters for logging is advantageous because:
 
- They handle cross-cutting concerns centrally
- Avoid code duplication across multiple resource methods
- Ensure consistent logging for all requests and responses
- Improve maintainability and readability
If logging is implemented inside each method, it leads to repetitive and cluttered code. Filters provide a cleaner and more scalable approach.
