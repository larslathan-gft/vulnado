# Documentation: `Comment.java`

## Overview
The `Comment` class is part of the `com.scalesec.vulnado` package and provides functionality for managing comments in a database. It includes methods for creating, fetching, and deleting comments, as well as persisting them to a PostgreSQL database. The class interacts with the database using JDBC.

---

## Class Details

### Class: `Comment`
The `Comment` class represents a comment entity with attributes such as `id`, `username`, `body`, and `created_on`. It provides methods for CRUD operations on the `comments` table in the database.

---

### Attributes

| Attribute Name | Type       | Description                                      |
|----------------|------------|--------------------------------------------------|
| `id`           | `String`   | Unique identifier for the comment.              |
| `username`     | `String`   | Username of the person who created the comment. |
| `body`         | `String`   | Content of the comment.                         |
| `created_on`   | `Timestamp`| Timestamp indicating when the comment was created. |

---

### Constructor

#### `Comment(String id, String username, String body, Timestamp created_on)`
Initializes a new `Comment` object with the provided attributes.

| Parameter      | Type       | Description                                      |
|----------------|------------|--------------------------------------------------|
| `id`           | `String`   | Unique identifier for the comment.              |
| `username`     | `String`   | Username of the person who created the comment. |
| `body`         | `String`   | Content of the comment.                         |
| `created_on`   | `Timestamp`| Timestamp indicating when the comment was created. |

---

### Methods

#### `static Comment create(String username, String body)`
Creates a new comment and persists it to the database.

| Parameter      | Type       | Description                                      |
|----------------|------------|--------------------------------------------------|
| `username`     | `String`   | Username of the person creating the comment.    |
| `body`         | `String`   | Content of the comment.                         |

**Returns:**  
A `Comment` object if the creation is successful. Throws `BadRequest` or `ServerError` exceptions in case of failure.

---

#### `static List<Comment> fetch_all()`
Fetches all comments from the database.

**Returns:**  
A `List<Comment>` containing all comments retrieved from the database.

---

#### `static Boolean delete(String id)`
Deletes a comment from the database based on its `id`.

| Parameter      | Type       | Description                                      |
|----------------|------------|--------------------------------------------------|
| `id`           | `String`   | Unique identifier of the comment to be deleted. |

**Returns:**  
`true` if the deletion is successful, otherwise `false`.

---

#### `private Boolean commit() throws SQLException`
Persists the current `Comment` object to the database.

**Returns:**  
`true` if the insertion is successful, otherwise `false`.

---

## Insights

### Database Interaction
- The class interacts with a PostgreSQL database using JDBC. It assumes the existence of a `Postgres.connection()` method for obtaining database connections.
- The `comments` table is expected to have the following columns:
  - `id` (String)
  - `username` (String)
  - `body` (String)
  - `created_on` (Timestamp)

### Error Handling
- The `create` method throws custom exceptions (`BadRequest` and `ServerError`) to handle errors during comment creation.
- Other methods use `try-catch` blocks to handle exceptions, but error handling could be improved (e.g., logging or rethrowing exceptions).

### Potential Issues
- The `delete` method always returns `false` in the `finally` block, even if the deletion is successful. This is likely a bug.
- The `fetch_all` method does not close the `Statement` object, which could lead to resource leaks.
- SQL queries are hardcoded, which may pose a risk of SQL injection if user input is not sanitized properly.

### UUID for Comment IDs
- The `create` method generates unique IDs for comments using `UUID.randomUUID()`. This ensures that each comment has a globally unique identifier.

### Timestamp Handling
- The `created_on` attribute is set using the current system time when a comment is created.

### Dependencies
- The class depends on external classes such as `Postgres`, `BadRequest`, and `ServerError`, which are not defined in this file. Their implementation is assumed to provide database connection and exception handling functionality.

---

### Recommendations
- Fix the `delete` method to correctly return the result of the deletion operation.
- Ensure proper resource management by closing `Statement` and `ResultSet` objects in the `fetch_all` method.
- Consider parameterizing SQL queries to prevent SQL injection.
- Improve error handling by logging exceptions or rethrowing them with meaningful messages.
