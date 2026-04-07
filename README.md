# Pet Management Service

A robust REST API service for managing pet information, built with Spring Boot 3.5. This service provides comprehensive endpoints for creating, retrieving, updating, and deleting pet records with a clean, modern architecture.

## Features

- **Create Pet Records** - Add new pets with detailed information
- **Retrieve Pets** - Fetch individual pets by ID or retrieve all pets
- **Update Pet Information** - Modify existing pet records
- **Delete Pet Records** - Remove pets from the system
- **Request Validation** - Built-in validation for all input data
- **Exception Handling** - Comprehensive error handling with meaningful error messages
- **API Documentation** - Interactive Swagger UI for testing endpoints
- **Logging** - SLF4J logging for tracking operations and debugging

## Technology Stack

- **Framework**: Spring Boot 3.5.12
- **Language**: Java 17
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI 2.8.16
- **Logging**: SLF4J with Lombok
- **Validation**: Jakarta Validation
- **Build**: Maven 3.x

## Project Structure

```
src/
├── main/
│   ├── java/it/pippo/petmanagement/
│   │   ├── PetsApiApplication.java          # Application entry point
│   │   ├── controller/
│   │   │   └── PetController.java           # REST endpoints
│   │   ├── service/
│   │   │   └── PetService.java              # Business logic
│   │   ├── repository/
│   │   │   ├── PetRepository.java           # Repository interface
│   │   │   └── impl/
│   │   │       └── PetRepositoryImpl.java    # Repository implementation
│   │   ├── model/
│   │   │   └── Pet.java                     # Pet entity model
│   │   ├── dto/
│   │   │   ├── PetRequestDto.java           # Request DTO
│   │   │   └── PetResponseDto.java          # Response DTO
│   │   ├── mapper/
│   │   │   └── PetMapper.java               # Entity-DTO mapping
│   │   └── exceptions/
│   │       ├── GlobalExceptionHandler.java  # Global error handler
│   │       └── PetNotFoundException.java     # Custom exceptions
│   └── resources/
│       └── application.properties            # Application configuration
└── test/
    └── java/it/pippo/petmanagement/         # Unit tests
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd pet-management-service
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file directly:
   ```bash
   java -jar target/pets-api-0.0.1-SNAPSHOT.jar
   ```

## API Access

The application runs on **port 9192** and provides full API documentation through Swagger/OpenAPI UI.

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:9192/swagger-ui.html
```

### OpenAPI JSON

The OpenAPI specification is available at:
```
http://localhost:9192/v3/api-docs
```

## API Endpoints

### Pet Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/pets` | Create a new pet |
| `GET` | `/api/pets` | Retrieve all pets |
| `GET` | `/api/pets/{id}` | Retrieve a pet by ID |
| `PUT` | `/api/pets/{id}` | Update a pet |
| `DELETE` | `/api/pets/{id}` | Delete a pet |

### Pet Attributes

- **id** - Unique identifier (Long)
- **name** - Pet's name (String, required)
- **species** - Pet's species/type (String, required)
- **age** - Pet's age (Integer)
- **ownerName** - Name of the pet's owner (String)

## Usage Examples

### Create a Pet
```bash
curl -X POST http://localhost:9192/api/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Buddy",
    "species": "Dog",
    "age": 3,
    "ownerName": "John Doe"
  }'
```

### Get All Pets
```bash
curl http://localhost:9192/api/pets
```

### Get Pet by ID
```bash
curl http://localhost:9192/api/pets/1
```

### Update a Pet
```bash
curl -X PUT http://localhost:9192/api/pets/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Buddy",
    "species": "Dog",
    "age": 4,
    "ownerName": "John Doe"
  }'
```

### Delete a Pet
```bash
curl -X DELETE http://localhost:9192/api/pets/1
```

## Configuration

The application is configured via `application.properties`:

```properties
spring.application.name=pets-api
server.port=9192
```

You can override these properties by:
- Setting environment variables
- Passing command-line arguments
- Creating an `application-{profile}.properties` file for different environments

## Error Handling

The API implements comprehensive error handling with appropriate HTTP status codes:

- **201 Created** - Pet successfully created
- **200 OK** - Request successful
- **204 No Content** - Pet successfully deleted
- **400 Bad Request** - Invalid request data
- **404 Not Found** - Pet not found
- **500 Internal Server Error** - Server error

Error responses include meaningful messages to help with debugging.

## Testing

Run the unit tests with:
```bash
mvn test
```

The project includes test classes for:
- Service layer functionality
- Application context loading



