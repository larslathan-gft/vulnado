# Documentation: `CommentsController.java`

## Overview
The `CommentsController` class is a RESTful controller implemented using the Spring Boot framework. It provides endpoints for managing comments, including fetching, creating, and deleting comments. The controller includes authentication mechanisms and error handling for invalid or server-related issues.

---

## Class: `CommentsController`

### Annotations
- **`@RestController`**: Marks the class as a RESTful controller, enabling automatic serialization of responses to JSON.
- **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration feature.
- **`@CrossOrigin(origins = "*")`**: Allows cross-origin requests from any domain.

### Fields
| Field Name | Type   | Description                                                                 |
|------------|--------|-----------------------------------------------------------------------------|
| `secret`   | String | A secret value injected from the application's configuration (`app.secret`). |

### Endpoints

#### 1. **GET `/comments`**
Fetches all comments.

| Parameter Name | Type   | Source         | Description                                      |
|----------------|--------|----------------|--------------------------------------------------|
| `x-auth-token` | String | Request Header | Authentication token used for authorization.    |

**Response**:  
Returns a list of `Comment` objects.

**Logic**:
- Validates the `x-auth-token` using `User.assertAuth(secret, token)`.
- Calls `Comment.fetch_all()` to retrieve all comments.

---

#### 2. **POST `/comments`**
Creates a new comment.

| Parameter Name | Type            | Source         | Description                                      |
|----------------|-----------------|----------------|--------------------------------------------------|
| `x-auth-token` | String          | Request Header | Authentication token used for authorization.    |
| `input`        | `CommentRequest`| Request Body   | Contains the username and body of the comment.  |

**Response**:  
Returns the created `Comment` object.

**Logic**:
- Calls `Comment.create(input.username, input.body)` to create a new comment.

---

#### 3. **DELETE `/comments/{id}`**
Deletes a comment by its ID.

| Parameter Name | Type   | Source         | Description                                      |
|----------------|--------|----------------|--------------------------------------------------|
| `x-auth-token` | String | Request Header | Authentication token used for authorization.    |
| `id`           | String | Path Variable  | The ID of the comment to be deleted.            |

**Response**:  
Returns a `Boolean` indicating whether the deletion was successful.

**Logic**:
- Calls `Comment.delete(id)` to delete the comment.

---

## Class: `CommentRequest`

### Description
A data structure representing the request body for creating a comment.

### Fields
| Field Name | Type   | Description                          |
|------------|--------|--------------------------------------|
| `username` | String | The username of the comment author. |
| `body`     | String | The content of the comment.         |

---

## Class: `BadRequest`

### Description
Represents a custom exception for HTTP 400 (Bad Request) errors.

### Constructor
| Parameter Name | Type   | Description                     |
|----------------|--------|---------------------------------|
| `exception`    | String | The error message to display.  |

---

## Class: `ServerError`

### Description
Represents a custom exception for HTTP 500 (Internal Server Error) errors.

### Constructor
| Parameter Name | Type   | Description                     |
|----------------|--------|---------------------------------|
| `exception`    | String | The error message to display.  |

---

## Insights

1. **Authentication**:  
   The controller uses a custom authentication mechanism (`User.assertAuth(secret, token)`) to validate requests. This ensures that only authorized users can access the endpoints.

2. **Cross-Origin Resource Sharing (CORS)**:  
   The `@CrossOrigin(origins = "*")` annotation allows requests from any domain, which is useful for enabling frontend applications hosted on different domains to interact with the API.

3. **Error Handling**:  
   Custom exceptions (`BadRequest` and `ServerError`) are defined to handle specific HTTP error scenarios, improving the clarity of error responses.

4. **Data Structure**:  
   The `CommentRequest` class is a simple data structure used for deserializing JSON request bodies when creating comments.

5. **Dependency Injection**:  
   The `@Value("${app.secret}")` annotation demonstrates the use of Spring's dependency injection to retrieve configuration values.

6. **Scalability**:  
   The design of the controller is modular, making it easy to extend with additional endpoints or features in the future.
