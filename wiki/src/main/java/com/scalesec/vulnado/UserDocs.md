# User.java: User Management and Authentication

## Overview
The `User` class is responsible for managing user information and handling authentication processes. It includes methods for generating JWT tokens, asserting authentication, and fetching user details from a database.

## Process Flow
```mermaid
flowchart TD
    A["User Class"] --> B["User Constructor"]
    A --> C["token(secret)"]
    A --> D["assertAuth(secret, token)"]
    A --> E["fetch(un)"]
    E --> F["Postgres.connection()"]
    F --> G["Database"]
    E --> H["Execute SQL Query"]
    H --> I["ResultSet"]
    I --> J["Create User Object"]
```

## Insights
- The `User` class includes attributes for `id`, `username`, and `hashedPassword`.
- The `token` method generates a JWT token using the provided secret key.
- The `assertAuth` method validates the provided JWT token using the secret key.
- The `fetch` method retrieves user details from the database based on the username.

## Dependencies
```mermaid
flowchart LR
    User --- |"Calls"| Postgres_connection
    User --- |"Uses"| io_jsonwebtoken_Jwts
    User --- |"Uses"| io_jsonwebtoken_JwtParser
    User --- |"Uses"| io_jsonwebtoken_SignatureAlgorithm
    User --- |"Uses"| io_jsonwebtoken_security_Keys
    User --- |"Uses"| javax_crypto_SecretKey
    User --- |"Uses"| java_sql_Connection
    User --- |"Uses"| java_sql_Statement
    User --- |"Uses"| java_sql_ResultSet
```

- `Postgres.connection`: Establishes a connection to the PostgreSQL database.
- `io.jsonwebtoken.Jwts`: Used for building and parsing JWT tokens.
- `io.jsonwebtoken.JwtParser`: Used for parsing JWT tokens.
- `io.jsonwebtoken.SignatureAlgorithm`: Specifies the algorithm for signing JWT tokens.
- `io.jsonwebtoken.security.Keys`: Generates secret keys for signing JWT tokens.
- `javax.crypto.SecretKey`: Represents the secret key used for signing JWT tokens.
- `java.sql.Connection`: Represents a connection to the database.
- `java.sql.Statement`: Used for executing SQL queries.
- `java.sql.ResultSet`: Represents the result set of a SQL query.

## Data Manipulation (SQL)
### Table: users
| Attribute  | Type   | Description                  |
|------------|--------|------------------------------|
| user_id    | String | Unique identifier for the user|
| username   | String | Username of the user         |
| password   | String | Hashed password of the user  |

- `users`: Fetches user details based on the username. The SQL operation performed is `SELECT`.

## Vulnerabilities
- **SQL Injection**: The `fetch` method constructs the SQL query using string concatenation, which is vulnerable to SQL injection. It should use prepared statements to prevent this vulnerability.
- **Sensitive Data Exposure**: The `token` method uses the secret key directly from the input, which might expose sensitive data if not handled properly. It should ensure the secret key is securely managed.
- **Exception Handling**: The `assertAuth` method prints the stack trace and throws an `Unauthorized` exception, which might expose sensitive information. It should handle exceptions more securely.
