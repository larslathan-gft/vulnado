# Documentation: CommentsController.java

## Overview
The `CommentsController` class is a RESTful controller implemented using Spring Boot. It provides endpoints for managing comments, including fetching, creating, and deleting comments. The controller also includes mechanisms for authentication and error handling.

---

## Class: `CommentsController`

### Annotations
- **`@RestController`**: Marks the class as a RESTful controller, enabling automatic serialization of responses to JSON.
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
- **Description**: Fetches all comments after validating the authentication token.
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
  - Calls `Comment.create(input.username, input.body)` to create the comment.
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
  - Returns a `Boolean` indicating success or failure.

---

## Class: `CommentRequest`

### Description
A data structure representing the request payload for creating a comment.

### Fields
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `username` | String | The username of the commenter. |
| `body`     | String | The content of the comment.    |

---

## Class: `BadRequest`

### Description
A custom exception class for handling bad requests.

### Annotations
- **`@ResponseStatus(HttpStatus.BAD_REQUEST)`**: Maps the exception to a `400 Bad Request` HTTP status.

### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | The error message for the exception. |

---

## Class: `ServerError`

### Description
A custom exception class for handling server errors.

### Annotations
- **`@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)`**: Maps the exception to a `500 Internal Server Error` HTTP status.

### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | The error message for the exception. |

---

## Insights

1. **Authentication**: The controller uses a secret value (`app.secret`) and an authentication token (`x-auth-token`) to validate requests. This ensures that only authorized users can access the endpoints.
2. **Cross-Origin Resource Sharing (CORS)**: The `@CrossOrigin` annotation allows requests from any origin, which is useful for enabling communication between different domains but may pose security risks if not properly configured.
3. **Error Handling**: Custom exceptions (`BadRequest` and `ServerError`) are used to provide meaningful HTTP status codes and error messages for client and server errors.
4. **Data Structure**: The `CommentRequest` class is a simple data structure for encapsulating the request payload for creating comments.
5. **Dependency Injection**: The `@Value` annotation is used to inject configuration properties, promoting flexibility and separation of concerns.
6. **Scalability**: The design of the controller supports scalability by separating concerns (authentication, data fetching, creation, and deletion).
