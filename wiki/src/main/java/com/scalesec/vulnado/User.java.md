# Documentation: `User.java`

## Overview
The `User` class is part of the `com.scalesec.vulnado` package and provides functionality for user management, including token generation, authentication, and database interaction. It integrates with JSON Web Tokens (JWT) for secure authentication and interacts with a PostgreSQL database to fetch user details.

---

## Class: `User`

### Attributes
| Attribute       | Type   | Description                                      |
|------------------|--------|--------------------------------------------------|
| `id`            | String | Unique identifier for the user.                  |
| `username`      | String | Username of the user.                            |
| `hashedPassword`| String | Hashed password of the user for secure storage.  |

### Constructor
```java
public User(String id, String username, String hashedPassword)
```
- **Parameters**:
  - `id`: Unique identifier for the user.
  - `username`: Username of the user.
  - `hashedPassword`: Hashed password of the user.
- **Description**: Initializes a `User` object with the provided attributes.

---

### Methods

#### `token(String secret)`
```java
public String token(String secret)
```
- **Parameters**:
  - `secret`: A secret key used for signing the JWT.
- **Returns**: A signed JWT string.
- **Description**: Generates a JWT token for the user using the provided secret key. The token's subject is set to the user's `username`.
- **Dependencies**:
  - Uses `io.jsonwebtoken` library for JWT creation.
  - Utilizes HMAC SHA key for signing the token.

---

#### `assertAuth(String secret, String token)`
```java
public static void assertAuth(String secret, String token)
```
- **Parameters**:
  - `secret`: The secret key used to verify the token.
  - `token`: The JWT token to be validated.
- **Description**: Validates the provided JWT token using the secret key. If the token is invalid, an `Unauthorized` exception is thrown.
- **Error Handling**:
  - Catches exceptions during token parsing and throws a custom `Unauthorized` exception with the error message.

---

#### `fetch(String un)`
```java
public static User fetch(String un)
```
- **Parameters**:
  - `un`: The username to search for in the database.
- **Returns**: A `User` object if the username exists in the database; otherwise, `null`.
- **Description**: Fetches user details from the PostgreSQL database based on the provided username.
- **Implementation Details**:
  - Establishes a connection to the database using `Postgres.connection()`.
  - Executes a SQL query to retrieve user details.
  - Constructs a `User` object from the retrieved data.
- **Error Handling**:
  - Catches and logs exceptions during database interaction.
  - Ensures the database connection is closed after execution.

---

## Insights

### Security Concerns
1. **SQL Injection Vulnerability**:
   - The `fetch` method constructs SQL queries using string concatenation, making it vulnerable to SQL injection attacks. Use prepared statements to mitigate this risk.

2. **Hardcoded Secret Key**:
   - The `token` and `assertAuth` methods rely on a secret key passed as a parameter. Ensure the secret key is securely managed and not hardcoded or exposed.

3. **Exception Handling**:
   - The `assertAuth` method prints stack traces, which may expose sensitive information. Consider logging errors securely without exposing internal details.

---

### Dependencies
- **Libraries**:
  - `io.jsonwebtoken`: Used for JWT creation and validation.
  - `javax.crypto`: Used for cryptographic operations.
- **Database**:
  - Interacts with a PostgreSQL database via the `Postgres.connection()` method.

---

### Potential Improvements
1. **Use Prepared Statements**:
   - Replace the string concatenation in the `fetch` method with prepared statements to prevent SQL injection.

2. **Token Expiry**:
   - Add an expiration time to the JWT tokens for enhanced security.

3. **Custom Exception Handling**:
   - Replace generic exception handling with custom exceptions for better error management.

4. **Database Connection Management**:
   - Use a connection pool or ensure proper resource management to avoid potential connection leaks.

---

### Related Classes
- **`Postgres`**:
  - Provides the `connection()` method used to interact with the database. Ensure this class is properly implemented and handles connection pooling efficiently.

- **`Unauthorized`**:
  - A custom exception class used in the `assertAuth` method. Ensure this class is defined and appropriately handles unauthorized access scenarios.
