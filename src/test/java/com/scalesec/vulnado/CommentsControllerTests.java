package com.scalesec.vulnado;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentsControllerTests {

    @Value("${app.secret}")
    private String secret;

    private final String validToken = "valid-token";
    private final String invalidToken = "invalid-token";

    // Mocking dependencies
    private final User userMock = mock(User.class);
    private final Comment commentMock = mock(Comment.class);

    private CommentsController createController() {
        CommentsController controller = new CommentsController();
        controller.secret = secret;
        return controller;
    }

    @Test
    public void comments_WithValidToken_ShouldReturnComments() {
        // Arrange
        CommentsController controller = createController();
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment());
        mockComments.add(new Comment());

        when(userMock.assertAuth(secret, validToken)).thenReturn(true);
        when(commentMock.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = controller.comments(validToken);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "The result should contain 2 comments");
    }

    @Test
    public void comments_WithInvalidToken_ShouldThrowException() {
        // Arrange
        CommentsController controller = createController();

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, invalidToken);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> controller.comments(invalidToken), "Should throw an unauthorized exception");
    }

    @Test
    public void createComment_WithValidInput_ShouldReturnCreatedComment() {
        // Arrange
        CommentsController controller = createController();
        CommentRequest request = new CommentRequest();
        request.username = "testUser";
        request.body = "This is a test comment";

        Comment mockComment = new Comment();
        when(commentMock.create(request.username, request.body)).thenReturn(mockComment);

        // Act
        Comment result = controller.createComment(validToken, request);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(mockComment, result, "The result should match the created comment");
    }

    @Test
    public void createComment_WithInvalidInput_ShouldThrowException() {
        // Arrange
        CommentsController controller = createController();
        CommentRequest request = new CommentRequest();
        request.username = null; // Invalid input
        request.body = "This is a test comment";

        doThrow(new BadRequest("Invalid input")).when(commentMock).create(request.username, request.body);

        // Act & Assert
        assertThrows(BadRequest.class, () -> controller.createComment(validToken, request), "Should throw a bad request exception");
    }

    @Test
    public void deleteComment_WithValidId_ShouldReturnTrue() {
        // Arrange
        CommentsController controller = createController();
        String commentId = "123";

        when(commentMock.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = controller.deleteComment(validToken, commentId);

        // Assert
        assertTrue(result, "The result should be true");
    }

    @Test
    public void deleteComment_WithInvalidId_ShouldReturnFalse() {
        // Arrange
        CommentsController controller = createController();
        String commentId = "invalid-id";

        when(commentMock.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = controller.deleteComment(validToken, commentId);

        // Assert
        assertFalse(result, "The result should be false");
    }
}
