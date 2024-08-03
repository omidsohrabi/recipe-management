# Recipe Management Application

## Overview

This application is designed to manage recipes. It provides a RESTful API for creating, updating, retrieving, deleting, and searching for recipes. The application is built using Java 17 and Spring Boot and follows the Onion Architecture pattern to ensure a clear separation of concerns and high maintainability.

## Architectural Choices

### Onion Architecture

The Onion Architecture is used to achieve a clean separation of concerns, making the system more maintainable, testable, and scalable. The key layers in this architecture are:

1. **Domain Layer**: Contains the core business logic and domain models.
2. **Service Layer**: Contains the application-specific logic such as services.
3. **Repository Layer**: Contains the repository implementations and data access logic.
4. **Application Layer**: Contains the REST controllers and handles HTTP requests and DTOs.

### Project Structure

- **api**
    - **config**: Contains open api configuration.
    - **dto**: Contains Data Transfer Objects (DTOs) used for communication with clients.
    - **exception**: Contains Exception Handlers.
    - **mapper**: Contains mappers for converting between domain models and DTOs.
    - and REST controller for handling HTTP requests.
- **domain**
    - **exception**: Contains core Exception classes.
    - **mapper**: Contains mappers for converting between domain models and entity models.
    - **model**: Contains the core business models.
- **repository**: Contains the JPA entity models and the repository interfaces for data access.
- **service**: Contains the service classes that implement the application logic.

## Running the Application

### Prerequisites

- Java 17 or later
- Maven 3.6.0 or later

### Steps to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/omidsohrabi/abn-technical-assessment
   cd recipe-management

2. **Build the Application**
   ```bash
   mvn clean install
   
3. **Run the Application**
   ```bash
    mvn spring-boot:run
   
4. **Access the API Documentation**

    Once the application is running, you can access the API documentation at http://localhost:8080/swagger-ui.html.
