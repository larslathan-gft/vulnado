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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

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
        assertEquals("Comments should match", expectedComments, actualComments);
        verify(user, times(1)).assertAuth(secret, token);
        verify(Comment, times(1)).fetch_all();
    }

    @Test
    public void createComment_ShouldCreateAndReturnComment() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";
        Comment expectedComment = new Comment();

        when(Comment.create(input.username, input.body)).thenReturn(expectedComment);

        // Act
        Comment actualComment = commentsController.createComment(token, input);

        // Assert
        assertEquals("Created comment should match", expectedComment, actualComment);
        verify(Comment, times(1)).create(input.username, input.body);
    }

    @Test
    public void deleteComment_ShouldDeleteAndReturnTrue() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Comment should be deleted", result);
        verify(Comment, times(1)).delete(commentId);
    }

    @Test(expected = BadRequest.class)
    public void createComment_ShouldThrowBadRequest() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "";
        input.body = "";

        // Act
        commentsController.createComment(token, input);
    }

    @Test(expected = ServerError.class)
    public void deleteComment_ShouldThrowServerError() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        when(Comment.delete(commentId)).thenThrow(new ServerError("Internal Server Error"));

        // Act
        commentsController.deleteComment(token, commentId);
    }
}
