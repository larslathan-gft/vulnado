# Documentation: `CommentsController.java`

## Overview
The `CommentsController` class is a RESTful controller implemented using the Spring Boot framework. It provides endpoints for managing comments, including fetching, creating, and deleting comments. The controller also includes mechanisms for authentication and error handling.

---

## Class: `CommentsController`

### Annotations
- **`@RestController`**: Indicates that this class is a REST controller, handling HTTP requests and returning JSON responses.
- **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration feature.
- **`@CrossOrigin(origins = "*")`**: Allows cross-origin requests from any domain.

### Fields
| Field Name | Type   | Description                                                                 |
|------------|--------|-----------------------------------------------------------------------------|
| `secret`   | String | A secret value injected from application properties (`app.secret`) for authentication. |

### Methods

#### `comments`
- **HTTP Method**: `GET`
- **Endpoint**: `/comments`
- **Produces**: `application/json`
- **Parameters**:
  - `@RequestHeader("x-auth-token") String token`: Authentication token passed in the request header.
- **Description**: Fetches all comments after verifying the authentication token.
- **Logic**:
  - Calls `User.assertAuth(secret, token)` to validate the token.
  - Returns a list of comments by invoking `Comment.fetch_all()`.

#### `createComment`
- **HTTP Method**: `POST`
- **Endpoint**: `/comments`
- **Produces**: `application/json`
- **Consumes**: `application/json`
- **Parameters**:
  - `@RequestHeader("x-auth-token") String token`: Authentication token passed in the request header.
  - `@RequestBody CommentRequest input`: JSON payload containing the comment details.
- **Description**: Creates a new comment using the provided username and body.
- **Logic**:
  - Calls `Comment.create(input.username, input.body)` to create a new comment.
  - Returns the created `Comment` object.

#### `deleteComment`
- **HTTP Method**: `DELETE`
- **Endpoint**: `/comments/{id}`
- **Produces**: `application/json`
- **Parameters**:
  - `@RequestHeader("x-auth-token") String token`: Authentication token passed in the request header.
  - `@PathVariable("id") String id`: ID of the comment to be deleted.
- **Description**: Deletes a comment by its ID.
- **Logic**:
  - Calls `Comment.delete(id)` to delete the comment.
  - Returns a `Boolean` indicating the success of the operation.

---

## Class: `CommentRequest`

### Description
A data structure representing the request payload for creating a comment.

### Fields
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `username` | String | The username of the commenter. |
| `body`     | String | The content of the comment.    |

### Implements
- **`Serializable`**: Ensures that instances of `CommentRequest` can be serialized.

---

## Class: `BadRequest`

### Description
A custom exception class representing HTTP 400 (Bad Request) errors.

### Annotations
- **`@ResponseStatus(HttpStatus.BAD_REQUEST)`**: Maps this exception to a 400 Bad Request response.

### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | The error message for the exception. |

---

## Class: `ServerError`

### Description
A custom exception class representing HTTP 500 (Internal Server Error) errors.

### Annotations
- **`@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)`**: Maps this exception to a 500 Internal Server Error response.

### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | The error message for the exception. |

---

## Insights

1. **Authentication**: The controller relies on a secret value (`app.secret`) and an authentication token (`x-auth-token`) for securing endpoints. The `User.assertAuth` method is used for token validation, but its implementation is not provided in this snippet.

2. **Cross-Origin Resource Sharing (CORS)**: All endpoints are configured to allow requests from any origin (`@CrossOrigin(origins = "*")`). This is useful for enabling access from different domains but may pose security risks if not properly managed.

3. **Error Handling**: Custom exceptions (`BadRequest` and `ServerError`) are defined to handle specific HTTP error responses. These exceptions improve the clarity of error reporting.

4. **Comment Management**: The `Comment` class is assumed to provide static methods (`fetch_all`, `create`, and `delete`) for managing comments. These methods encapsulate the logic for interacting with the underlying data store.

5. **Scalability**: The controller is designed to handle basic CRUD operations for comments. However, additional features like pagination, filtering, or rate-limiting may be required for large-scale applications.

6. **Security Considerations**: The use of a shared secret for authentication may not be ideal for distributed systems. Consider implementing more robust authentication mechanisms, such as OAuth or JWT, for enhanced security.
