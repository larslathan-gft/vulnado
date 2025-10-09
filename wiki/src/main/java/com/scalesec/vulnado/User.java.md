# Documentation: `User` Class

## Overview

The `User` class is part of the `com.scalesec.vulnado` package and represents a user entity in the system. It provides functionality for user authentication, token generation, and fetching user data from a database. The class interacts with a PostgreSQL database and uses the `io.jsonwebtoken` library for JSON Web Token (JWT) operations.

---

## Class: `User`

### Fields

| Field Name       | Type     | Description                                      |
|------------------|----------|--------------------------------------------------|
| `id`             | `String` | Unique identifier for the user.                 |
| `username`       | `String` | Username of the user.                           |
| `hashedPassword` | `String` | Hashed password of the user.                    |

---

### Constructor

#### `User(String id, String username, String hashedPassword)`

- **Description**: Initializes a new `User` object with the provided `id`, `username`, and `hashedPassword`.
- **Parameters**:
  - `id`: Unique identifier for the user.
  - `username`: Username of the user.
  - `hashedPassword`: Hashed password of the user.

---

### Methods

#### `String token(String secret)`

- **Description**: Generates a JSON Web Token (JWT) for the user using the provided secret key.
- **Parameters**:
  - `secret`: A secret key used to sign the JWT.
- **Returns**: A signed JWT as a `String`.

#### `static void assertAuth(String secret, String token)`

- **Description**: Validates a given JWT token using the provided secret key. If the token is invalid, an `Unauthorized` exception is thrown.
- **Parameters**:
  - `secret`: A secret key used to validate the JWT.
  - `token`: The JWT to be validated.
- **Throws**: `Unauthorized` exception if the token is invalid.

#### `static User fetch(String un)`

- **Description**: Fetches a user from the database by their username.
- **Parameters**:
  - `un`: The username of the user to fetch.
- **Returns**: A `User` object if the user is found, otherwise `null`.
- **Database Interaction**:
  - Connects to a PostgreSQL database using the `Postgres.connection()` method.
  - Executes a SQL query to retrieve user details from the `users` table.
  - Constructs a `User` object with the retrieved data.

---

## Insights

1. **Security Concerns**:
   - The `fetch` method constructs SQL queries using string concatenation, which makes it vulnerable to **SQL Injection** attacks. It is recommended to use **prepared statements** to prevent this vulnerability.
   - The `token` and `assertAuth` methods rely on a secret key for signing and verifying JWTs. Ensure the secret key is securely stored and not hardcoded in the application.

2. **Error Handling**:
   - The `fetch` method catches exceptions and prints stack traces but does not rethrow or handle them in a structured way. This could lead to silent failures. Consider implementing proper error handling and logging mechanisms.
   - The `assertAuth` method throws a custom `Unauthorized` exception, but the `Unauthorized` class is not defined in the provided code. Ensure this class is implemented elsewhere in the project.

3. **Database Connection Management**:
   - The `fetch` method closes the database connection but does not close the `Statement` object, which could lead to resource leaks. Use a `try-with-resources` block to ensure proper resource management.

4. **JWT Implementation**:
   - The `token` method uses the `io.jsonwebtoken` library to generate JWTs. The `setSubject` method sets the username as the subject of the token. Ensure that additional claims or expiration times are added to the token for enhanced security.

5. **Code Design**:
   - The `fetch` method is tightly coupled with the database schema (`users` table). Any changes to the schema would require updates to this method.
   - The `User` class combines data representation (fields) with business logic (methods). Consider separating these concerns for better maintainability.

---

## Dependencies

The `User` class relies on the following libraries and classes:

| Dependency                  | Purpose                                                                 |
|-----------------------------|-------------------------------------------------------------------------|
| `java.sql.Connection`       | Establishes a connection to the database.                              |
| `java.sql.Statement`        | Executes SQL queries.                                                  |
| `java.sql.ResultSet`        | Processes the results of SQL queries.                                  |
| `io.jsonwebtoken.*`         | Handles JWT creation and validation.                                   |
| `javax.crypto.SecretKey`    | Represents the secret key used for signing JWTs.                       |
| `Postgres.connection()`     | Provides a connection to the PostgreSQL database (assumed external).   |
| `Unauthorized` (custom)     | Custom exception for unauthorized access (not defined in the snippet). |

---

## Potential Enhancements

- **Use Prepared Statements**: Replace string concatenation in SQL queries with prepared statements to prevent SQL Injection.
- **Add Token Expiration**: Include an expiration claim (`exp`) in the JWT for enhanced security.
- **Improve Error Handling**: Implement structured error handling and logging for better debugging and reliability.
- **Resource Management**: Use `try-with-resources` for database connections and statements to avoid resource leaks.
- **Custom Exception Definition**: Ensure the `Unauthorized` exception is properly defined and documented.
