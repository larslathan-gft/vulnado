# Documentation: `Postgres.java`

## Overview
The `Postgres` class provides functionality for interacting with a PostgreSQL database. It includes methods for establishing a database connection, setting up the database schema, inserting seed data, and hashing passwords using the MD5 algorithm. This class is designed to initialize and manage a database for a user and comment system.

---

## Features
### 1. Database Connection
The `connection()` method establishes a connection to a PostgreSQL database using credentials and connection details retrieved from environment variables:
- **Environment Variables Used**:
  - `PGHOST`: Hostname of the PostgreSQL server.
  - `PGDATABASE`: Name of the database.
  - `PGUSER`: Username for authentication.
  - `PGPASSWORD`: Password for authentication.

### 2. Database Setup
The `setup()` method:
- Creates two tables (`users` and `comments`) if they do not already exist.
- Cleans up any existing data in these tables.
- Inserts seed data into the tables for testing or initialization purposes.

### 3. MD5 Hashing
The `md5(String input)` method generates an MD5 hash for a given input string. This is used to securely store passwords in the database.

### 4. Data Insertion
- **`insertUser(String username, String password)`**: Inserts a new user into the `users` table with a hashed password.
- **`insertComment(String username, String body)`**: Inserts a new comment into the `comments` table.

---

## Database Schema
The `setup()` method defines the following schema:

### `users` Table
| Column Name   | Data Type      | Constraints                          |
|---------------|----------------|--------------------------------------|
| `user_id`     | `VARCHAR(36)`  | Primary Key                         |
| `username`    | `VARCHAR(50)`  | Unique, Not Null                    |
| `password`    | `VARCHAR(50)`  | Not Null                            |
| `created_on`  | `TIMESTAMP`    | Not Null                            |
| `last_login`  | `TIMESTAMP`    | Optional                            |

### `comments` Table
| Column Name   | Data Type      | Constraints                          |
|---------------|----------------|--------------------------------------|
| `id`          | `VARCHAR(36)`  | Primary Key                         |
| `username`    | `VARCHAR(36)`  | Foreign Key (linked to `users`)     |
| `body`        | `VARCHAR(500)` | Not Null                            |
| `created_on`  | `TIMESTAMP`    | Not Null                            |

---

## Seed Data
The `setup()` method inserts the following seed data:

### Users
| Username | Password              |
|----------|-----------------------|
| `admin`  | `!!SuperSecretAdmin!!`|
| `alice`  | `AlicePassword!`      |
| `bob`    | `BobPassword!`        |
| `eve`    | `$EVELknev^l`         |
| `rick`   | `!GetSchwifty!`       |

### Comments
| Username | Comment         |
|----------|-----------------|
| `rick`   | `cool dog m8`   |
| `alice`  | `OMG so cute!`  |

---

## Insights
### Security Concerns
1. **Password Hashing**:
   - The MD5 algorithm is used for hashing passwords. MD5 is considered cryptographically weak and vulnerable to collision attacks. It is recommended to use stronger hashing algorithms like bcrypt, PBKDF2, or Argon2 for password storage.

2. **Environment Variables**:
   - Database credentials are retrieved from environment variables, which is a good practice for avoiding hardcoding sensitive information. Ensure these variables are securely managed.

3. **SQL Injection**:
   - The use of `PreparedStatement` for inserting data helps mitigate SQL injection risks.

### Scalability
- The schema design supports scalability by using UUIDs for primary keys, ensuring uniqueness across distributed systems.

### Error Handling
- The class uses `try-catch` blocks for error handling but does not implement robust logging or recovery mechanisms. Consider integrating a logging framework like Log4j or SLF4J for better error tracking.

### Code Maintainability
- The `connection()` method is reused across multiple operations, promoting code reuse and maintainability.
- The `setup()` method combines schema creation, data cleanup, and seed data insertion, which may limit flexibility. Consider separating these operations for better modularity.

---

## Usage
### Setting Up the Database
Call the `setup()` method to initialize the database schema and insert seed data:
```java
Postgres.setup();
```

### Inserting a User
Use the `insertUser()` method to add a new user:
```java
Postgres.insertUser("new_user", "secure_password");
```

### Inserting a Comment
Use the `insertComment()` method to add a new comment:
```java
Postgres.insertComment("new_user", "This is a comment.");
```
