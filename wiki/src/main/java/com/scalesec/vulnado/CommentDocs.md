# Comment.java: Comment Management

## Overview
The `Comment` class is responsible for managing comments, including creating, fetching, and deleting comments from a PostgreSQL database. It includes methods for creating new comments, fetching all comments, and deleting comments by ID.

## Process Flow
```mermaid
flowchart TD
    A["Comment Class"] --> B["create(username, body)"]
    B --> C["Generate UUID and Timestamp"]
    C --> D["commit()"]
    D --> E{"Commit Successful?"}
    E --> |"Yes"| F["Return Comment"]
    E --> |"No"| G["Throw BadRequest Exception"]
    A --> H["fetch_all()"]
    H --> I["Execute SQL Query"]
    I --> J["Iterate ResultSet"]
    J --> K["Create Comment Objects"]
    K --> L["Return List of Comments"]
    A --> M["delete(id)"]
    M --> N["Prepare SQL Statement"]
    N --> O["Execute Update"]
    O --> P{"Delete Successful?"}
    P --> |"Yes"| Q["Return True"]
    P --> |"No"| R["Return False"]
```

## Insights
- The `Comment` class interacts with a PostgreSQL database to perform CRUD operations on comments.
- The `create` method generates a unique ID and timestamp for each comment and attempts to commit it to the database.
- The `fetch_all` method retrieves all comments from the database and returns them as a list.
- The `delete` method removes a comment from the database based on its ID.
- The `commit` method is a private method used to insert a new comment into the database.

## Dependencies
```mermaid
flowchart LR
    Comment --- |"Calls"| Postgres
    Comment --- |"Throws"| BadRequest
    Comment --- |"Throws"| ServerError
```

- `Postgres`: Used to establish a connection to the PostgreSQL database.
- `BadRequest`: Exception thrown when a comment cannot be saved.
- `ServerError`: Exception thrown when there is a server error.

## Data Manipulation (SQL)
### Table: comments
| Attribute   | Type      | Description                        |
|-------------|-----------|------------------------------------|
| id          | String    | Unique identifier for the comment  |
| username    | String    | Username of the commenter          |
| body        | String    | Content of the comment             |
| created_on  | Timestamp | Timestamp when the comment was created |

### SQL Operations
- `INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)`: Inserts a new comment into the `comments` table.
- `SELECT * FROM comments`: Retrieves all comments from the `comments` table.
- `DELETE FROM comments WHERE id = ?`: Deletes a comment from the `comments` table based on its ID.

## Vulnerabilities
- **SQL Injection**: The `fetch_all` method uses raw SQL queries which can be vulnerable to SQL injection. Using prepared statements can mitigate this risk.
- **Resource Management**: The `fetch_all` and `delete` methods do not properly close the database connection in the `finally` block, which can lead to resource leaks.
- **Exception Handling**: The `delete` method always returns `false` even if the operation is successful due to the `finally` block. This should be corrected to return the actual result of the operation.
