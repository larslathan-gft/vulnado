package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

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

    // Helper method to create a mock Comment object
    private Comment createMockComment(String id, String username, String body) {
        Comment comment = mock(Comment.class);
        when(comment.getId()).thenReturn(id);
        when(comment.getUsername()).thenReturn(username);
        when(comment.getBody()).thenReturn(body);
        return comment;
    }

    @Test
    public void comments_ShouldReturnAllComments_WhenAuthorized() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(createMockComment("1", "user1", "This is a comment."));
        mockComments.add(createMockComment("2", "user2", "Another comment."));

        doNothing().when(userMock).assertAuth(secret, token);
        when(Comment.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match", 2, result.size());
        verify(userMock, times(1)).assertAuth(secret, token);
        verify(commentMock, times(1)).fetch_all();
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenUnauthorized() {
        // Arrange
        String token = "invalid-token";
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.comments(token);

        // Assert
        // Exception is expected
    }

    @Test
    public void createComment_ShouldCreateComment_WhenValidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "This is a new comment.";

        Comment mockComment = createMockComment("1", input.username, input.body);

        doNothing().when(userMock).assertAuth(secret, token);
        when(Comment.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Username should match", input.username, result.getUsername());
        assertEquals("Body should match", input.body, result.getBody());
        verify(userMock, times(1)).assertAuth(secret, token);
        verify(commentMock, times(1)).create(input.username, input.body);
    }

    @Test(expected = ResponseStatusException.class)
    public void createComment_ShouldThrowException_WhenUnauthorized() {
        // Arrange
        String token = "invalid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "This is a new comment.";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.createComment(token, input);

        // Assert
        // Exception is expected
    }

    @Test
    public void deleteComment_ShouldDeleteComment_WhenValidId() {
        // Arrange
        String token = "valid-token";
        String commentId = "1";

        doNothing().when(userMock).assertAuth(secret, token);
        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Result should be true", result);
        verify(userMock, times(1)).assertAuth(secret, token);
        verify(commentMock, times(1)).delete(commentId);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_ShouldThrowException_WhenUnauthorized() {
        // Arrange
        String token = "invalid-token";
        String commentId = "1";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, token);

        // Act
        commentsController.deleteComment(token, commentId);

        // Assert
        // Exception is expected
    }
}
