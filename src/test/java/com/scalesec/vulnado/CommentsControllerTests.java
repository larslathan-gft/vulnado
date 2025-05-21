import com.scalesec.vulnado.Comment;
import com.scalesec.vulnado.CommentRequest;
import com.scalesec.vulnado.CommentsController;
import com.scalesec.vulnado.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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
    private User user;

    @Mock
    private Comment comment;

    @Value("${app.secret}")
    private String secret;

    @Before
    public void setUp() {
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
        assertEquals("The returned comments list should match the expected list", expectedComments, actualComments);
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
        assertEquals("The created comment should match the expected comment", expectedComment, actualComment);
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
        assertTrue("The comment should be deleted successfully", result);
    }

    @Test(expected = BadRequest.class)
    public void comments_ShouldThrowBadRequestOnInvalidToken() {
        // Arrange
        String token = "invalid-token";
        doThrow(new BadRequest("Invalid token")).when(user).assertAuth(secret, token);

        // Act
        commentsController.comments(token);
    }

    @Test(expected = ServerError.class)
    public void createComment_ShouldThrowServerErrorOnFailure() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";

        when(Comment.create(input.username, input.body)).thenThrow(new ServerError("Server error"));

        // Act
        commentsController.createComment(token, input);
    }

    @Test(expected = BadRequest.class)
    public void deleteComment_ShouldThrowBadRequestOnInvalidToken() {
        // Arrange
        String token = "invalid-token";
        String commentId = "123";
        doThrow(new BadRequest("Invalid token")).when(user).assertAuth(secret, token);

        // Act
        commentsController.deleteComment(token, commentId);
    }
}
