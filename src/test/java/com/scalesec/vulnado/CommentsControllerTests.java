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

    @Mock
    private User user;

    @Mock
    private Comment comment;

    @InjectMocks
    private CommentsController commentsController;

    @Value("${app.secret}")
    private String secret;

    public CommentsControllerTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void comments_ShouldReturnAllComments() {
        // Arrange
        String token = "valid-token";
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment());
        expectedComments.add(new Comment());

        doNothing().when(user).assertAuth(secret, token);
        when(Comment.fetch_all()).thenReturn(expectedComments);

        // Act
        List<Comment> actualComments = commentsController.comments(token);

        // Assert
        assertEquals("The comments list should match the expected list", expectedComments, actualComments);
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenTokenIsInvalid() {
        // Arrange
        String token = "invalid-token";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(user).assertAuth(secret, token);

        // Act
        commentsController.comments(token);
    }

    @Test
    public void createComment_ShouldCreateAndReturnComment() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";
        Comment expectedComment = new Comment();

        doNothing().when(user).assertAuth(secret, token);
        when(Comment.create(input.username, input.body)).thenReturn(expectedComment);

        // Act
        Comment actualComment = commentsController.createComment(token, input);

        // Assert
        assertEquals("The created comment should match the expected comment", expectedComment, actualComment);
    }

    @Test
    public void deleteComment_ShouldDeleteAndReturnTrue() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        Boolean expectedResult = true;

        doNothing().when(user).assertAuth(secret, token);
        when(Comment.delete(commentId)).thenReturn(expectedResult);

        // Act
        Boolean actualResult = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("The comment should be deleted successfully", actualResult);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_ShouldThrowException_WhenTokenIsInvalid() {
        // Arrange
        String token = "invalid-token";
        String commentId = "123";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(user).assertAuth(secret, token);

        // Act
        commentsController.deleteComment(token, commentId);
    }
}
