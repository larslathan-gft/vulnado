# Documentation: `LoginController.java`

## Overview
The `LoginController.java` file implements a RESTful API endpoint for user authentication. It is built using the Spring Boot framework and provides functionality to validate user credentials and return a token upon successful authentication. The file also includes supporting classes for request and response handling, as well as custom exception handling.

---

## File Metadata
- **File Name**: `LoginController.java`

---

## Components

### 1. **LoginController**
The main controller class that handles the `/login` endpoint.

#### Key Features:
- **Endpoint**: `/login`
- **HTTP Method**: POST
- **Consumes**: `application/json`
- **Produces**: `application/json`
- **Cross-Origin Support**: Enabled for all origins (`@CrossOrigin(origins = "*")`).

#### Logic:
1. Accepts a `LoginRequest` object containing `username` and `password`.
2. Fetches the user details using `User.fetch(input.username)`.
3. Validates the provided password by comparing its MD5 hash with the stored hashed password.
4. If validation succeeds, generates a token using the application's secret and returns it in a `LoginResponse`.
5. If validation fails, throws an `Unauthorized` exception with the message "Access Denied".

#### Dependencies:
- **Spring Boot Annotations**:
  - `@RestController`: Marks the class as a REST controller.
  - `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration.
  - `@CrossOrigin`: Allows cross-origin requests.
  - `@RequestMapping`: Maps the `/login` endpoint to the `login` method.
- **Spring Framework**:
  - `@Value`: Injects the application's secret from configuration.

---

### 2. **LoginRequest**
A data structure representing the incoming request payload for the `/login` endpoint.

#### Fields:
| Field Name | Type     | Description                     |
|------------|----------|---------------------------------|
| `username` | `String` | The username of the user.       |
| `password` | `String` | The plaintext password provided.|

#### Characteristics:
- Implements `Serializable` for object serialization.

---

### 3. **LoginResponse**
A data structure representing the response payload for the `/login` endpoint.

#### Fields:
| Field Name | Type     | Description                     |
|------------|----------|---------------------------------|
| `token`    | `String` | The authentication token.       |

#### Constructor:
- Accepts a `String` message (token) and assigns it to the `token` field.

#### Characteristics:
- Implements `Serializable` for object serialization.

---

### 4. **Unauthorized**
A custom exception class used to handle unauthorized access.

#### Features:
- Annotated with `@ResponseStatus(HttpStatus.UNAUTHORIZED)` to return a 401 HTTP status code when thrown.
- Constructor accepts an exception message and passes it to the superclass (`RuntimeException`).

---

## Insights

### Security Considerations:
1. **Password Storage**: The code assumes that passwords are stored as MD5 hashes. MD5 is considered cryptographically insecure and should be replaced with a stronger hashing algorithm like bcrypt or Argon2.
2. **Token Generation**: The `User.token(secret)` method is used to generate tokens. Ensure that the token generation mechanism is secure and follows best practices (e.g., using JWT with proper signing and expiration).
3. **Cross-Origin Requests**: Allowing all origins (`@CrossOrigin(origins = "*")`) can expose the API to potential security risks. Consider restricting origins to trusted domains.

### Error Handling:
- The `Unauthorized` exception provides a clear mechanism for handling authentication failures. However, additional logging or monitoring may be necessary to track failed login attempts.

### Scalability:
- The current implementation fetches user details and validates passwords synchronously. For high-traffic applications, consider optimizing database queries and implementing caching mechanisms.

### Dependency Injection:
- The application's secret is injected using `@Value("${app.secret}")`. Ensure that sensitive configuration values are securely managed and not exposed in version control.

---

## Summary of Endpoints

| Endpoint | HTTP Method | Request Body       | Response Body       | Description                     |
|----------|-------------|--------------------|---------------------|---------------------------------|
| `/login` | POST        | `LoginRequest`    | `LoginResponse`     | Authenticates a user and returns a token. |
