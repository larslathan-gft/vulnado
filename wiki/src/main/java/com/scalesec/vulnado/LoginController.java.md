# Documentation: `LoginController.java`

## Overview
The `LoginController.java` file is part of a Spring Boot application and provides functionality for user authentication. It defines a REST API endpoint for handling login requests, validates user credentials, and generates a token for successful authentication. The file also includes supporting classes for request and response handling, as well as custom exception handling.

---

## File Metadata
- **File Name**: `LoginController.java`

---

## Components

### 1. **LoginController**
The main controller class that handles login requests.

#### Annotations:
- `@RestController`: Indicates that this class is a REST controller.
- `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration feature.
- `@Value("${app.secret}")`: Injects the value of the `app.secret` property from the application's configuration.

#### Endpoint:
- **Path**: `/login`
- **HTTP Method**: `POST`
- **Consumes**: `application/json`
- **Produces**: `application/json`
- **Cross-Origin**: Allows requests from any origin (`@CrossOrigin(origins = "*")`).

#### Method: `login`
- **Input**: `LoginRequest` object containing `username` and `password`.
- **Logic**:
  - Fetches the user details using `User.fetch(input.username)`.
  - Compares the hashed password (`Postgres.md5(input.password)`) with the stored hashed password (`user.hashedPassword`).
  - If the credentials match, generates a token using `user.token(secret)` and returns it in a `LoginResponse`.
  - If the credentials do not match, throws an `Unauthorized` exception.
- **Output**: `LoginResponse` containing the authentication token.

---

### 2. **LoginRequest**
A data structure representing the login request payload.

#### Fields:
| Field Name | Type     | Description                  |
|------------|----------|------------------------------|
| `username` | `String` | The username of the user.    |
| `password` | `String` | The password of the user.    |

#### Characteristics:
- Implements `Serializable` for object serialization.

---

### 3. **LoginResponse**
A data structure representing the login response payload.

#### Fields:
| Field Name | Type     | Description                  |
|------------|----------|------------------------------|
| `token`    | `String` | The authentication token.    |

#### Constructor:
- Accepts a `String` message (token) and assigns it to the `token` field.

#### Characteristics:
- Implements `Serializable` for object serialization.

---

### 4. **Unauthorized**
A custom exception class for handling unauthorized access.

#### Annotations:
- `@ResponseStatus(HttpStatus.UNAUTHORIZED)`: Maps this exception to the HTTP 401 Unauthorized status code.

#### Constructor:
- Accepts a `String` exception message and passes it to the superclass (`RuntimeException`).

---

## Insights

### Security Considerations:
1. **Hardcoded Secret**: The `secret` is injected from the application's configuration (`app.secret`). Ensure this value is securely stored and not exposed in the source code or logs.
2. **Password Hashing**: The password is hashed using `Postgres.md5`. Verify that this hashing mechanism is secure and up-to-date with modern cryptographic standards.
3. **Cross-Origin Requests**: The `@CrossOrigin(origins = "*")` annotation allows requests from any origin, which may pose security risks. Consider restricting origins to trusted domains.

### Error Handling:
- The `Unauthorized` exception provides a clear mechanism for handling failed authentication attempts, returning an appropriate HTTP status code (401).

### Serialization:
- Both `LoginRequest` and `LoginResponse` implement `Serializable`, which is useful for object serialization, especially in distributed systems.

### Dependencies:
- The code relies on external classes (`User` and `Postgres`) for user fetching and password hashing. Ensure these classes are implemented securely and efficiently.

### Scalability:
- The current implementation fetches user details and performs password hashing synchronously. For high-traffic applications, consider optimizing these operations or using asynchronous processing.

---

## Summary Table

| Component         | Type            | Purpose                                      |
|-------------------|-----------------|----------------------------------------------|
| `LoginController` | Controller      | Handles login requests and authentication.   |
| `LoginRequest`    | Data Structure  | Represents the login request payload.        |
| `LoginResponse`   | Data Structure  | Represents the login response payload.       |
| `Unauthorized`    | Exception Class | Handles unauthorized access errors.          |
