# Postgres.java Documentation

## Overview

The `Postgres` class provides functionality to interact with a PostgreSQL database. It includes methods for establishing a database connection, setting up the database schema, inserting user and comment data, and generating MD5 hash values for secure password storage. This class is designed to manage user and comment data for an application.

---

## Features

### 1. Database Connection
The `connection()` method establishes a connection to a PostgreSQL database using environment variables for configuration:
- `PGHOST`: Hostname of the PostgreSQL server.
- `PGDATABASE`: Name of the database.
- `PGUSER`: Username for authentication.
- `PGPASSWORD`: Password for authentication.

### 2. Database Setup
The `setup()` method:
- Creates two tables (`users` and `comments`) if they do not already exist.
- Cleans up any existing data in these tables.
- Inserts seed data for users and comments.

### 3. MD5 Hashing
The `md5(String input)` method generates an MD5 hash for a given input string. This is used to securely store user passwords in the database.

### 4. Data Insertion
- `insertUser(String username, String password)`: Inserts a new user into the `users` table with a hashed password.
- `insertComment(String username, String body)`: Inserts a new comment into the `comments` table.

---

## Database Schema

The `setup()` method creates the following tables:

### `users` Table
| Column Name  | Data Type      | Constraints                     |
|--------------|----------------|----------------------------------|
| user_id      | VARCHAR(36)    | Primary Key                     |
| username     | VARCHAR(50)    | Unique, Not Null                |
| password     | VARCHAR(50)    | Not Null                        |
| created_on   | TIMESTAMP      | Not Null                        |
| last_login   | TIMESTAMP      | Optional                        |

### `comments` Table
| Column Name  | Data Type      | Constraints                     |
|--------------|----------------|----------------------------------|
| id           | VARCHAR(36)    | Primary Key                     |
| username     | VARCHAR(36)    | Foreign Key (references `users`)|
| body         | VARCHAR(500)   | No constraints                  |
| created_on   | TIMESTAMP      | Not Null                        |

---

## Methods

### `connection()`
- **Purpose**: Establishes a connection to the PostgreSQL database.
- **Returns**: `Connection` object.
- **Error Handling**: Prints stack trace and exits the program on failure.

### `setup()`
- **Purpose**: Sets up the database schema, cleans up existing data, and inserts seed data.
- **Operations**:
  - Creates `users` and `comments` tables if they do not exist.
  - Deletes all existing data in these tables.
  - Inserts predefined user and comment data.

### `md5(String input)`
- **Purpose**: Generates an MD5 hash for the given input string.
- **Returns**: A 32-character hexadecimal string representing the MD5 hash.
- **Error Handling**: Throws a `RuntimeException` if the MD5 algorithm is not available.

### `insertUser(String username, String password)`
- **Purpose**: Inserts a new user into the `users` table.
- **Parameters**:
  - `username`: The username of the user.
  - `password`: The plaintext password of the user (hashed before insertion).
- **Error Handling**: Prints stack trace on failure.

### `insertComment(String username, String body)`
- **Purpose**: Inserts a new comment into the `comments` table.
- **Parameters**:
  - `username`: The username of the user who made the comment.
  - `body`: The content of the comment.
- **Error Handling**: Prints stack trace on failure.

---

## Insights

1. **Security Concerns**:
   - Passwords are hashed using MD5, which is considered insecure for password storage due to vulnerabilities to brute-force and collision attacks. A more secure hashing algorithm like `bcrypt` or `SHA-256` with salting should be used.
   - The database credentials are retrieved from environment variables, which is a good practice for securing sensitive information.

2. **Database Design**:
   - The `users` table uses `user_id` as the primary key, while `username` is unique. This allows for flexibility in changing usernames without affecting relationships.
   - The `comments` table does not enforce a foreign key constraint on `username`, which could lead to data integrity issues if a user is deleted.

3. **Error Handling**:
   - The program exits on critical errors (e.g., database connection failure), which may not be ideal for production environments. Consider implementing a retry mechanism or logging errors without terminating the application.

4. **Seed Data**:
   - The `setup()` method inserts predefined users and comments. This is useful for testing but should be removed or modified for production environments.

5. **Scalability**:
   - The current implementation does not use connection pooling, which could lead to performance issues under high load. Consider using a connection pool library like HikariCP.

6. **Hardcoded Values**:
   - The seed data includes hardcoded usernames and passwords, which may not be suitable for production use.
