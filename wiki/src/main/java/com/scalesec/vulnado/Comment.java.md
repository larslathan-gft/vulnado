# Documentation: `Comment.java`

## Overview
The `Comment` class is part of the `com.scalesec.vulnado` package and provides functionality for managing comments in a database. It includes methods for creating, fetching, and deleting comments, as well as persisting them to a PostgreSQL database. This class interacts with the database using JDBC.

---

## Class: `Comment`

### Fields
| Field Name   | Type         | Description                                      |
|--------------|--------------|--------------------------------------------------|
| `id`         | `String`     | Unique identifier for the comment.              |
| `username`   | `String`     | Username of the user who created the comment.   |
| `body`       | `String`     | Content of the comment.                         |
| `created_on` | `Timestamp`  | Timestamp indicating when the comment was created. |

---

### Constructor
#### `Comment(String id, String username, String body, Timestamp created_on)`
Creates a new `Comment` object with the specified attributes.

**Parameters:**
- `id`: Unique identifier for the comment.
- `username`: Username of the user who created the comment.
- `body`: Content of the comment.
- `created_on`: Timestamp indicating when the comment was created.

---

### Methods

#### `static Comment create(String username, String body)`
Creates a new comment and saves it to the database.

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
Deletes a comment from the database based on its ID.

**Parameters:**
- `id`: The unique identifier of the comment to be deleted.

**Returns:**
- `true` if the comment was successfully deleted.
- `false` otherwise.

**Behavior:**
- Executes a SQL `DELETE` statement using a prepared statement.

---

#### `private Boolean commit() throws SQLException`
Commits the current `Comment` object to the database.

**Returns:**
- `true` if the comment was successfully saved.
- `false` otherwise.

**Behavior:**
- Executes a SQL `INSERT` statement using a prepared statement.

---

## Insights

### Security Concerns
1. **SQL Injection Risk**: 
   - The `fetch_all` method uses raw SQL queries (`Statement`) instead of prepared statements, which can expose the application to SQL injection attacks. It is recommended to use `PreparedStatement` for all database queries.
   
2. **Error Handling**:
   - The `delete` method always returns `false` in the `finally` block, even if the operation succeeds. This can lead to incorrect behavior. The return statement should be moved outside the `finally` block.

3. **Exception Management**:
   - Exceptions are caught and printed using `e.printStackTrace()` but are not logged or rethrown in some cases. Proper logging mechanisms should be implemented.

---

### Database Dependency
The class relies on a `Postgres.connection()` method to establish a connection to the database. This method is assumed to be implemented elsewhere in the application.

---

### UUID for Comment IDs
The `create` method generates a unique identifier for each comment using `UUID.randomUUID()`. This ensures that each comment has a globally unique ID.

---

### Timestamp Handling
The `create` method uses `java.util.Date` to generate the current timestamp and converts it to `java.sql.Timestamp`. This ensures compatibility with the database's `TIMESTAMP` type.

---

### Potential Improvements
1. **Connection Management**:
   - Database connections are not closed in the `delete` and `commit` methods. This can lead to resource leaks. Use `try-with-resources` to ensure connections are properly closed.

2. **Validation**:
   - The `create` method does not validate the `username` or `body` parameters. Adding validation checks can prevent invalid data from being saved to the database.

3. **Error Messages**:
   - The error messages in exceptions are generic. Providing more specific error messages can help in debugging and user feedback.

---

### Dependencies
- **JDBC**: Used for database interaction.
- **Postgres.connection()**: A custom method assumed to provide a database connection.
- **UUID**: Used for generating unique IDs.
- **Custom Exceptions**:
  - `BadRequest`: Thrown when a comment cannot be saved.
  - `ServerError`: Thrown for unexpected errors.

---

### Usage Example
```java
// Create a new comment
Comment newComment = Comment.create("john_doe", "This is a sample comment.");

// Fetch all comments
List<Comment> allComments = Comment.fetch_all();

// Delete a comment by ID
Boolean isDeleted = Comment.delete("some-unique-id");
```
