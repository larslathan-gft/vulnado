# Documentation: `Comment.java`

## Overview
The `Comment` class is part of the `com.scalesec.vulnado` package and provides functionality for managing comments in a database. It includes methods for creating, fetching, and deleting comments, as well as persisting them to a PostgreSQL database. This class interacts with the database using JDBC.

---

## Class: `Comment`

### Fields
| Field Name   | Type         | Description                                                                 |
|--------------|--------------|-----------------------------------------------------------------------------|
| `id`         | `String`     | Unique identifier for the comment, generated using `UUID`.                 |
| `username`   | `String`     | Username of the user who created the comment.                              |
| `body`       | `String`     | Content of the comment.                                                    |
| `created_on` | `Timestamp`  | Timestamp indicating when the comment was created.                         |

---

### Constructor
#### `Comment(String id, String username, String body, Timestamp created_on)`
Initializes a new `Comment` object with the provided values.

**Parameters:**
- `id`: Unique identifier for the comment.
- `username`: Username of the user who created the comment.
- `body`: Content of the comment.
- `created_on`: Timestamp indicating when the comment was created.

---

### Methods

#### `static Comment create(String username, String body)`
Creates a new comment and persists it to the database.

**Parameters:**
- `username`: Username of the user creating the comment.
- `body`: Content of the comment.

**Returns:**
- A `Comment` object representing the newly created comment.

**Throws:**
- `BadRequest`: If the comment cannot be saved.
- `ServerError`: If an unexpected error occurs during the operation.

---

#### `static List<Comment> fetch_all()`
Fetches all comments from the database.

**Returns:**
- A `List<Comment>` containing all comments retrieved from the database.

**Behavior:**
- Executes a SQL query to fetch all rows from the `comments` table.
- Maps each row to a `Comment` object and adds it to the list.

---

#### `static Boolean delete(String id)`
Deletes a comment from the database based on its unique identifier.

**Parameters:**
- `id`: The unique identifier of the comment to be deleted.

**Returns:**
- `true` if the comment was successfully deleted.
- `false` otherwise.

**Behavior:**
- Executes a SQL `DELETE` statement with the provided `id`.

---

#### `private Boolean commit() throws SQLException`
Persists the current `Comment` object to the database.

**Returns:**
- `true` if the comment was successfully saved.
- `false` otherwise.

**Behavior:**
- Executes a SQL `INSERT` statement to add the comment to the `comments` table.

---

## Insights

### Database Interaction
- The class heavily relies on JDBC for database operations. It uses `Connection`, `Statement`, and `PreparedStatement` objects to interact with a PostgreSQL database.
- The database table `comments` is expected to have the following columns:
  - `id` (String)
  - `username` (String)
  - `body` (String)
  - `created_on` (Timestamp)

### Error Handling
- The `create` method throws custom exceptions (`BadRequest` and `ServerError`) to handle specific error scenarios.
- Other methods log errors using `e.printStackTrace()` but do not propagate exceptions, which may lead to silent failures.

### Potential Issues
1. **Error Handling in `delete` Method:**
   - The `delete` method always returns `false` in the `finally` block, even if the deletion was successful. This is likely a bug.
   
2. **SQL Injection Risk:**
   - The `fetch_all` method uses raw SQL queries (`stmt.executeQuery(query)`), which could be vulnerable to SQL injection if user input is incorporated into the query in the future.

3. **Resource Management:**
   - Database connections are not always closed properly, especially in the `fetch_all` method. This could lead to resource leaks.

4. **Thread Safety:**
   - The class is not thread-safe. Concurrent access to shared resources (e.g., database connections) could lead to unpredictable behavior.

### Recommendations
- Use a connection pool to manage database connections efficiently.
- Replace raw SQL queries with parameterized queries to prevent SQL injection.
- Improve error handling to ensure exceptions are propagated or logged consistently.
- Refactor the `delete` method to correctly return the result of the operation.
