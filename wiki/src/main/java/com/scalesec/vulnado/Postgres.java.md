# Documentation for `Postgres.java`

## Overview

The `Postgres` class provides utility methods to interact with a PostgreSQL database. It includes functionality for establishing a database connection, setting up the database schema, inserting seed data, and hashing passwords using the MD5 algorithm. This class is designed to initialize and manage a database for a user and comment system.

---

## Class: `Postgres`

### Methods

#### 1. `public static Connection connection()`
Establishes a connection to the PostgreSQL database using environment variables for configuration.

- **Environment Variables Used**:
  - `PGHOST`: Hostname of the PostgreSQL server.
  - `PGDATABASE`: Name of the database.
  - `PGUSER`: Username for authentication.
  - `PGPASSWORD`: Password for authentication.

- **Returns**: 
  - A `Connection` object to interact with the database.
  - Returns `null` if the connection fails.

- **Error Handling**:
  - Prints the stack trace and exits the program with status `1` if an exception occurs.

---

#### 2. `public static void setup()`
Sets up the database schema, cleans up existing data, and inserts seed data.

- **Functionality**:
  - Creates two tables:
    - `users`: Stores user information.
    - `comments`: Stores user comments.
  - Deletes all existing data in the `users` and `comments` tables.
  - Inserts predefined seed data for users and comments.

- **Seed Data**:
  - **Users**:
    | Username | Password            |
    |----------|---------------------|
    | admin    | !!SuperSecretAdmin!!|
    | alice    | AlicePassword!      |
    | bob      | BobPassword!        |
    | eve      | $EVELknev^l         |
    | rick     | !GetSchwifty!       |
  - **Comments**:
    | Username | Comment         |
    |----------|-----------------|
    | rick     | cool dog m8     |
    | alice    | OMG so cute!    |

- **Error Handling**:
  - Prints the exception and exits the program with status `1` if an error occurs.

---

#### 3. `public static String md5(String input)`
Generates an MD5 hash for the given input string.

- **Parameters**:
  - `input`: The string to be hashed.

- **Returns**:
  - A 32-character hexadecimal MD5 hash of the input string.

- **Error Handling**:
  - Throws a `RuntimeException` if the MD5 algorithm is not available.

- **Usage**:
  - Used to hash passwords before storing them in the database.

---

#### 4. `private static void insertUser(String username, String password)`
Inserts a new user into the `users` table.

- **Parameters**:
  - `username`: The username of the user.
  - `password`: The plaintext password of the user (hashed using MD5 before insertion).

- **Functionality**:
  - Generates a unique `user_id` using `UUID`.
  - Hashes the password using the `md5` method.
  - Inserts the user into the `users` table with the current timestamp as `created_on`.

- **Error Handling**:
  - Prints the stack trace if an exception occurs.

---

#### 5. `private static void insertComment(String username, String body)`
Inserts a new comment into the `comments` table.

- **Parameters**:
  - `username`: The username of the user who made the comment.
  - `body`: The content of the comment.

- **Functionality**:
  - Generates a unique `id` for the comment using `UUID`.
  - Inserts the comment into the `comments` table with the current timestamp as `created_on`.

- **Error Handling**:
  - Prints the stack trace if an exception occurs.

---

## Database Schema

### 1. `users` Table
| Column Name   | Data Type      | Constraints                     |
|---------------|----------------|----------------------------------|
| `user_id`     | `VARCHAR(36)`  | Primary Key                     |
| `username`    | `VARCHAR(50)`  | Unique, Not Null                |
| `password`    | `VARCHAR(50)`  | Not Null                        |
| `created_on`  | `TIMESTAMP`    | Not Null                        |
| `last_login`  | `TIMESTAMP`    | Nullable                        |

### 2. `comments` Table
| Column Name   | Data Type      | Constraints                     |
|---------------|----------------|----------------------------------|
| `id`          | `VARCHAR(36)`  | Primary Key                     |
| `username`    | `VARCHAR(36)`  | Foreign Key (to `users.username`)|
| `body`        | `VARCHAR(500)` | Not Null                        |
| `created_on`  | `TIMESTAMP`    | Not Null                        |

---

## Insights

1. **Security Concerns**:
   - The use of MD5 for password hashing is insecure and outdated. Consider using a stronger hashing algorithm like `bcrypt` or `PBKDF2` for password storage.
   - Storing passwords directly in the database, even if hashed, without salting increases vulnerability to rainbow table attacks.

2. **Environment Variables**:
   - The database connection relies on environment variables. Ensure these variables are securely managed and not exposed in the source code or logs.

3. **Error Handling**:
   - The program exits with status `1` on most exceptions, which may not be ideal for production environments. Consider implementing more robust error handling and logging mechanisms.

4. **Database Initialization**:
   - The `setup` method deletes all existing data in the `users` and `comments` tables. This behavior is suitable for development but may not be appropriate for production.

5. **Scalability**:
   - The current implementation is suitable for small-scale applications. For larger systems, consider optimizing database queries and connection management.

6. **UUID Usage**:
   - The use of `UUID` for primary keys ensures uniqueness and avoids potential conflicts in distributed systems.

7. **Prepared Statements**:
   - The use of `PreparedStatement` helps prevent SQL injection attacks, which is a good practice.

---
