# Documentation: `User.java`

## Overview
The `User` class is part of the `com.scalesec.vulnado` package and provides functionality for user management, including token generation, authentication, and database interaction. It encapsulates user-related data and operations, such as fetching user details from a database and verifying authentication tokens.

---

## Class: `User`

### Attributes
| Attribute       | Type     | Description                                      |
|------------------|----------|--------------------------------------------------|
| `id`            | `String` | Unique identifier for the user.                 |
| `username`      | `String` | Username of the user.                           |
| `hashedPassword`| `String` | Hashed password of the user.                    |

### Constructor
#### `User(String id, String username, String hashedPassword)`
Initializes a new `User` object with the provided `id`, `username`, and `hashedPassword`.

---

## Methods

### `String token(String secret)`
Generates a JSON Web Token (JWT) for the user using the provided secret key.

#### Parameters:
- `secret`: A `String` representing the secret key used for signing the token.

#### Returns:
- A `String` containing the generated JWT.

#### Implementation Details:
- Uses the `io.jsonwebtoken` library to create the JWT.
- The `username` is set as the subject of the token.
- The token is signed using the HMAC SHA algorithm.

---

### `static void assertAuth(String secret, String token)`
Validates the provided JWT token using the given secret key.

#### Parameters:
- `secret`: A `String` representing the secret key used for verifying the token.
- `token`: A `String` containing the JWT to be validated.

#### Throws:
- `Unauthorized`: Custom exception thrown if the token validation fails.

#### Implementation Details:
- Uses the `io.jsonwebtoken` library to parse and validate the token.
- If validation fails, an exception is caught, logged, and rethrown as an `Unauthorized` exception.

---

### `static User fetch(String un)`
Fetches a user from the database based on the provided username.

#### Parameters:
- `un`: A `String` representing the username to search for.

#### Returns:
- A `User` object containing the user's details if found, or `null` if no user matches the query.

#### Implementation Details:
- Establishes a connection to the database using `Postgres.connection()`.
- Executes a SQL query to retrieve user details from the `users` table.
- Constructs a `User` object using the retrieved data.
- Closes the database connection after the operation.

#### Notes:
- The SQL query is vulnerable to SQL injection due to direct concatenation of the `username` parameter into the query string.

---

## Insights

### Security Concerns
1. **SQL Injection Vulnerability**:
   - The `fetch` method directly concatenates the `username` parameter into the SQL query, making it susceptible to SQL injection attacks. Use prepared statements to mitigate this risk.

2. **Hardcoded Secret Key Handling**:
   - The `token` and `assertAuth` methods rely on a secret key passed as a parameter. Ensure the secret key is securely stored and managed to prevent unauthorized access.

3. **Exception Handling**:
   - The `assertAuth` method logs exceptions and rethrows them as `Unauthorized`. Ensure sensitive information is not exposed in logs.

### Best Practices
- **Use Prepared Statements**:
  Replace the direct SQL query in the `fetch` method with a prepared statement to prevent SQL injection.

- **Secure Key Management**:
  Store and manage the secret key securely using environment variables or a dedicated secrets management service.

- **Password Hashing**:
  Ensure the `hashedPassword` attribute uses a strong hashing algorithm (e.g., bcrypt) with proper salting.

### Dependencies
- **Database Connection**:
  The `fetch` method relies on the `Postgres.connection()` method, which is assumed to provide a valid database connection.

- **JWT Library**:
  The `io.jsonwebtoken` library is used for token generation and validation.

---

## Potential Enhancements
1. **Input Validation**:
   - Validate the `username` parameter in the `fetch` method to ensure it adheres to expected formats.

2. **Error Handling**:
   - Improve error handling in the `fetch` method to provide more meaningful feedback in case of database connection issues.

3. **Token Expiry**:
   - Add an expiration time to the generated JWT tokens for enhanced security.
