package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsControllerTests {

    @InjectMocks
    private CommentsController commentsController;

    @Mock
    private User userMock;

    @Mock
    private Comment commentMock;

    @Value("${app.secret}")
    private String secret;

    public CommentsControllerTests() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a mock CommentRequest
    private CommentRequest createMockCommentRequest(String username, String body) {
        CommentRequest request = new CommentRequest();
        request.username = username;
        request.body = body;
        return request;
    }

    @Test
    public void comments_ShouldReturnAllComments_WhenAuthIsValid() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment());
        mockComments.add(new Comment());

        doNothing().when(userMock).assertAuth(secret, token);
        when(Comment.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match mock comments size", mockComments.size(), result.size());
        verify(userMock, times(1)).assertAuth(secret, token);
        verify(commentMock, times(1)).fetch_all();
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenAuthIsInvalid() {
        // Arrange
        String token = "invalid-token";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.comments(token);

        // Assert
        // Exception is expected
    }

    @Test
    public void createComment_ShouldCreateComment_WhenInputIsValid() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = createMockCommentRequest("testUser", "testBody");
        Comment mockComment = new Comment();

        when(Comment.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
        verify(commentMock, times(1)).create(input.username, input.body);
    }

    @Test(expected = ResponseStatusException.class)
    public void createComment_ShouldThrowException_WhenInputIsInvalid() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = createMockCommentRequest(null, null);

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(commentMock).create(input.username, input.body);

        // Act
        commentsController.createComment(token, input);

        // Assert
        // Exception is expected
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentIsDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Result should be true", result);
        verify(commentMock, times(1)).delete(commentId);
    }

    @Test
    public void deleteComment_ShouldReturnFalse_WhenCommentIsNotDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        when(Comment.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertFalse("Result should be false", result);
        verify(commentMock, times(1)).delete(commentId);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_ShouldThrowException_WhenCommentIdIsInvalid() {
        // Arrange
        String token = "valid-token";
        String commentId = "invalid-id";

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(commentMock).delete(commentId);

        // Act
        commentsController.deleteComment(token, commentId);

        // Assert
        // Exception is expected
    }
}
