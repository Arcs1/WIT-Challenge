# Calculator API

This project is a RESTful API that provides basic calculator functionalities, developed to meet the requirements of the **Java Challenge 2024** issued by [WIT](https://www.wit-software.com/) to developers applying to the company. It supports the basic arithmetic operations for only 2 operands: addition, subtraction, multiplication and division.

## Features

### Functional Features

- **Basic Operations**:
    - Addition
    - Subtraction
    - Multiplication
    - Division
- Support for signed decimal numbers with arbitrary precision.
- Unique identifier to each REST request and includes it in response headers.

### Non-functional Features

- Two modules:
    - **Calculator**: Handles the core arithmetic logic.
    - **REST**: Exposes the API endpoints and interacts with the Calculator module via Apache Kafka.
- **Apache Kafka**: Utilized as a message broker to facilitate communication between the REST and Calculator modules. Note that Kafka is not implemented as part of this project; instead, the project uses an [**Apache Kafka Docker**](https://hub.docker.com/r/apache/kafka) container, configured and managed through Docker Compose.
- **Spring Boot**: Foundation for both modules.
- **Docker Support**: Includes Dockerfile and Docker Compose for containerized deployment.
- **Configuration**: Managed via `application.properties`.
- **SLF4J Logging**: Logs all input, output, and errors using Logback.
- **MDC Propagation**: Propagates the unique request identifier across modules and includes it in log messages.

## Prerequisites

Ensure the following software is installed:
- [Java 21+](https://www.oracle.com/java/technologies/downloads/#java21)
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

## Getting Started

### Building and Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/Arcs1/WIT-Challenge
   cd WIT-Challenge
   ```

2. Build and start the services using Docker Compose:
   ```bash
   docker-compose build
   docker-compose up
   ```

3. The API will be available at `http://localhost:8080/api/calculator`.

### API Endpoints

| Endpoint         | Method | Description                     | Example Request                              |
|------------------|--------|---------------------------------|----------------------------------------------|
| `/sum`           | GET    | Adds two numbers.               | `GET /sum?a=1.5&b=2.3`                       |
| `/subtract`      | GET    | Subtracts two numbers.          | `GET /subtract?a=5&b=3.2`                    |
| `/multiply`      | GET    | Multiplies two numbers.         | `GET /multiply?a=2.5&b=4`                    |
| `/divide`        | GET    | Divides two numbers.            | `GET /divide?a=10&b=2.5`                     |

#### Example Response

For the request:
```http
GET /sum?a=1.5&b=2.3 HTTP/1.1
Accept: application/json
```
Response:
```json
{
  "result": 3.8,
  "error": "None"
}
```

### Configuration

All configurations can be updated in the `application.properties` files located in each module, except for the logging configuration, which is set in the `logback-spring.xml` files.

### Logs

Logs are generated for all events, including input/output and errors. Each log entry includes a unique request identifier for traceability.

## Testing

The project includes unit tests to ensure the correctness of the calculator functionalities and Kafka communication. To run the tests, execute:
```bash
./gradlew test
```

## Docker Setup

### Dockerfile

Each module has its own Dockerfile for image creation.

### Docker Compose

The `docker-compose.yml` file sets up the following services:
- REST Module
- Calculator Module
- Kafka

## Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Apache Kafka](https://kafka.apache.org/)
- [Docker](https://www.docker.com/)
