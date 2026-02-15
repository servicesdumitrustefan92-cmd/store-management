# Store Management API

## Overview

This project implements a simple Store Management REST API built with
**Java 21** and **Spring Boot**.

The application acts as an internal store catalog management tool and
allows:

-   Creating products
-   Retrieving products by SKU
-   Updating product price

The API is secured using role-based access control and includes input
validation, global exception handling, and unit tests.

------------------------------------------------------------------------

## Features

-   Create product (ADMIN only)
-   Retrieve product by SKU (ADMIN, USER)
-   Update product price (ADMIN only)
-   Input validation using Jakarta Validation
-   Consistent JSON error responses
-   Basic Authentication with role-based access
-   Unit tests for business logic
-   In-memory database (H2) for zero-setup execution

------------------------------------------------------------------------

## Tech Stack

-   Java 21
-   Spring Boot 3.x
-   Spring Web
-   Spring Data JPA
-   Spring Security (Basic Auth)
-   H2 Database (in-memory)
-   JUnit 5 + Mockito

------------------------------------------------------------------------

## How to Run

### Run Tests

    ./mvnw clean test

### Start the Application

    ./mvnw spring-boot:run

The application will start on:

    http://localhost:8080

------------------------------------------------------------------------

## Authentication

The API uses HTTP Basic Authentication.

### Default Users

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | adminpass | ADMIN |
| user     | userpass  | USER  |
------------------------------------------------------------------------

## API Endpoints

### 1. Create Product (ADMIN only)

POST `/products`

    curl -i -u admin:adminpass -H "Content-Type: application/json" -d '{"sku":"TV-55-LED-001","name":"Samsung 55 inch LED TV","price":2499.99,"currency":"RON"}' http://localhost:8080/products

Response: 201 Created

------------------------------------------------------------------------

### 2. Get Product (ADMIN or USER)

GET `/products/{sku}`

    curl -i -u user:userpass  http://localhost:8080/products/TV-55-LED-001

Response: 200 OK

------------------------------------------------------------------------

### 3. Change Product Price (ADMIN only)

PATCH `/products/{sku}/price`

    curl -i -u admin:adminpass -H "Content-Type: application/json" -d '{"newPrice":1999.99}' http://localhost:8080/products/TV-55-LED-001/price

Response: 200 OK

------------------------------------------------------------------------

## Validation Rules

### Product Creation

-   `sku` must contain 3--40 uppercase letters, digits, or hyphens
-   `name` must be 2--120 characters
-   `price` must be positive
-   `currency` must be a 3-letter ISO code (e.g., RON, EUR)

### Price Update

-   `newPrice` must be positive

------------------------------------------------------------------------

## Error Handling

The API returns structured JSON error responses.

### Example: Validation Error (400)

    {
      "status": 400,
      "code": "VALIDATION_ERROR",
      "message": "Invalid request",
      "path": "/products",
      "details": [
        "price: must be greater than 0"
      ]
    }

### Error Codes

-   400 VALIDATION_ERROR
-   404 PRODUCT_NOT_FOUND
-   409 SKU_ALREADY_EXISTS
-   500 INTERNAL_ERROR

------------------------------------------------------------------------

## Database

The application uses an **H2 in-memory database**.

-   Schema is auto-generated (`ddl-auto=update`)
-   No external database setup required
-   Designed for demonstration and evaluation purposes


--------------------------------------------------------------------------

## Security Notes

-   Basic Authentication is used for simplicity.
-   Passwords use `{noop}` encoder for demonstration only.
-   In production:
    -   Passwords should be hashed (e.g., BCrypt)
    -   Credentials managed securely (e.g., secrets manager)
    -   OAuth2 / JWT recommended for authentication

-----------------------------------------------------------------------

## Design Decisions

-   DTOs implemented as Java 21 records for immutability and clarity.
-   Business logic isolated in a dedicated service layer.
-   Repository layer uses Spring Data JPA.
-   Global exception handling via `@RestControllerAdvice`.
-   `open-in-view` disabled to follow REST best practices.
-   Unit tests cover both success and error flows of service logic.

------------------------------------------------------------------------

## Build

To build the project:

    ./mvnw clean package

The generated artifact will be available in the `target` directory.
