# Documentation: LoginController

## Overview

The `LoginController` class is a RESTful controller implemented using the Spring Boot framework. It provides an endpoint for user authentication by validating login credentials and generating a token upon successful authentication. The controller interacts with a `User` class and a `Postgres` utility for user data retrieval and password hashing, respectively.

---

## File Metadata

- **File Name**: `LoginController.java`

---

## Components

### 1. **LoginController Class**
The main controller class that handles the `/login` endpoint.

#### Annotations:
- `@RestController`: Indicates that this class is a REST controller.
- `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration feature.
- `@Value("${app.secret}")`: Injects the application secret from the configuration file.

#### Fields:
- `private String secret`: A secret key used for token generation, injected from the application configuration.

#### Endpoint:
- **Path**: `/login`
- **HTTP Method**: `POST`
- **Consumes**: `application/json`
- **Produces**: `application/json`
- **CORS**: Allows cross-origin requests from any origin (`@CrossOrigin(origins = "*")`).

#### Method: `login`
- **Input**: 
  - `LoginRequest` object containing `username` and `password`.
- **Logic**:
  1. Fetches the user details using the `User.fetch()` method.
  2. Compares the hashed version of the input password (using `Postgres.md5()`) with the stored hashed password.
  3. If the passwords match, generates a token using the `user.token(secret)` method and returns it in a `LoginResponse` object.
  4. If the passwords do not match, throws an `Unauthorized` exception with the message "Access Denied".

---

### 2. **LoginRequest Class**
A data structure representing the login request payload.

#### Implements:
- `Serializable`: Allows the object to be serialized.

#### Fields:
- `String username`: The username provided by the client.
- `String password`: The password provided by the client.

---

### 3. **LoginResponse Class**
A data structure representing the login response payload.

#### Implements:
- `Serializable`: Allows the object to be serialized.

#### Fields:
- `String token`: The authentication token generated upon successful login.

#### Constructor:
- `LoginResponse(String msg)`: Initializes the `token` field with the provided message.

---

### 4. **Unauthorized Class**
A custom exception class for handling unauthorized access.

#### Annotations:
- `@ResponseStatus(HttpStatus.UNAUTHORIZED)`: Maps this exception to an HTTP 401 Unauthorized status code.

#### Constructor:
- `Unauthorized(String exception)`: Initializes the exception with a custom message.

---

## Insights

1. **Security Considerations**:
   - The `secret` field is injected from the application configuration, which is a good practice for managing sensitive data.
   - The use of `Postgres.md5()` for password hashing may not be secure. Modern password hashing algorithms like `bcrypt` or `Argon2` are recommended.
   - Allowing cross-origin requests from any origin (`@CrossOrigin(origins = "*")`) can expose the application to security risks. It is recommended to restrict origins to trusted domains.

2. **Error Handling**:
   - The `Unauthorized` exception is used to handle invalid login attempts, providing a clear and consistent error response.

3. **Scalability**:
   - The design separates the request and response payloads into distinct classes (`LoginRequest` and `LoginResponse`), which improves code maintainability and scalability.

4. **Dependencies**:
   - The code relies on external classes (`User` and `Postgres`) for user data retrieval and password hashing. These dependencies are not included in the provided code and should be implemented securely.

5. **Serialization**:
   - Both `LoginRequest` and `LoginResponse` implement `Serializable`, which is useful for transmitting objects over a network or saving them to a file.

---

## Summary Table

| **Component**       | **Description**                                                                 |
|----------------------|---------------------------------------------------------------------------------|
| `LoginController`    | REST controller for handling user login requests.                              |
| `LoginRequest`       | Data structure for login request payload.                                      |
| `LoginResponse`      | Data structure for login response payload containing the authentication token. |
| `Unauthorized`       | Custom exception for handling unauthorized access with HTTP 401 status.        |
