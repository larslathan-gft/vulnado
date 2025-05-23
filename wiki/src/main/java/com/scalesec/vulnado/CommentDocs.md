# Comment.java: Comment Management

## Overview
This class is responsible for managing comments, including creating, fetching, and deleting comments from a PostgreSQL database. It provides methods to create a new comment, fetch all comments, and delete a comment by its ID.

## Process Flow
```mermaid
flowchart TD
    A["Comment.create(username, body)"] --> B["Generate UUID and Timestamp"]
    B --> C["Create Comment Object"]
    C --> D["Commit Comment to Database"]
    D --> E["Return Comment Object"]
    
    F["Comment.fetch_all()"] --> G["Create SQL Query"]
    G --> H["Execute Query"]
    H --> I["Iterate ResultSet"]
    I --> J["Create Comment Objects"]
    J --> K["Return List of Comments"]
    
    L["Comment.delete(id)"] --> M["Create SQL Delete Statement"]
    M --> N["Prepare Statement with ID"]
    N --> O["Execute Update"]
    O --> P["Return Boolean Result"]
```

## Insights
- The `Comment` class interacts with a PostgreSQL database to manage comments.
- The `create` method generates a unique ID and timestamp for each new comment.
- The `fetch_all` method retrieves all comments from the database.
- The `delete` method removes a comment from the database based on its ID.
- The `commit` method is used internally to save a new comment to the database.

## Dependencies
```mermaid
flowchart LR
    Comment --- |"Calls"| Postgres
    Comment --- |"Throws"| BadRequest
    Comment --- |"Throws"| ServerError
```

- `Postgres`: Used to establish a connection to the PostgreSQL database.
- `BadRequest`: Thrown when a comment cannot be saved.
- `ServerError`: Thrown when there is a server error.

## Data Manipulation (SQL)
### Table: comments
| Attribute   | Type       | Description                        |
|-------------|------------|------------------------------------|
| id          | String     | Unique identifier for the comment  |
| username    | String     | Username of the commenter          |
| body        | String     | Content of the comment             |
| created_on  | Timestamp  | Timestamp when the comment was created |

### SQL Operations
- **INSERT**: Adds a new comment to the `comments` table.
- **SELECT**: Retrieves all comments from the `comments` table.
- **DELETE**: Removes a comment from the `comments` table based on its ID.

## Vulnerabilities
- **SQL Injection**: The `fetch_all` method uses a raw SQL query which is vulnerable to SQL injection. It is recommended to use prepared statements to mitigate this risk.
- **Resource Management**: The `fetch_all` and `delete` methods do not properly close the database resources (e.g., `Statement`, `Connection`). This can lead to resource leaks.
- **Error Handling**: The `delete` method always returns `false` due to the `finally` block. This should be corrected to return the actual result of the delete operation.
