package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTests {

    @Test
    public void create_ShouldReturnComment_WhenValidInput() {
        String username = "testUser";
        String body = "This is a test comment";

        Comment comment = Comment.create(username, body);

        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", username, comment.username);
        assertEquals("Body should match", body, comment.body);
        assertNotNull("Timestamp should not be null", comment.created_on);
    }

    @Test(expected = BadRequest.class)
    public void create_ShouldThrowBadRequest_WhenCommitFails() throws SQLException {
        String username = "testUser";
        String body = "This is a test comment";

        Comment comment = Mockito.spy(new Comment(UUID.randomUUID().toString(), username, body, new Timestamp(System.currentTimeMillis())));
        doReturn(false).when(comment).commit();

        Comment.create(username, body);
    }

    @Test(expected = ServerError.class)
    public void create_ShouldThrowServerError_WhenExceptionOccurs() throws SQLException {
        String username = "testUser";
        String body = "This is a test comment";

        Comment comment = Mockito.spy(new Comment(UUID.randomUUID().toString(), username, body, new Timestamp(System.currentTimeMillis())));
        doThrow(new SQLException("Database error")).when(comment).commit();

        Comment.create(username, body);
    }

    @Test
    public void fetch_all_ShouldReturnListOfComments() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("id")).thenReturn("1", "2");
        when(mockResultSet.getString("username")).thenReturn("user1", "user2");
        when(mockResultSet.getString("body")).thenReturn("body1", "body2");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        Postgres.setConnection(mockConnection);

        List<Comment> comments = Comment.fetch_all();

        assertEquals("Should return 2 comments", 2, comments.size());
        assertEquals("First comment username should match", "user1", comments.get(0).username);
        assertEquals("Second comment username should match", "user2", comments.get(1).username);
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentDeleted() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(mockConnection);

        boolean result = Comment.delete("1");

        assertTrue("Should return true when comment is deleted", result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentNotDeleted() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(mockConnection);

        boolean result = Comment.delete("1");

        assertFalse("Should return false when comment is not deleted", result);
    }

    @Test
    public void commit_ShouldReturnTrue_WhenCommentSaved() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(mockConnection);

        Comment comment = new Comment(UUID.randomUUID().toString(), "user", "body", new Timestamp(System.currentTimeMillis()));
        boolean result = comment.commit();

        assertTrue("Should return true when comment is saved", result);
    }

    @Test
    public void commit_ShouldReturnFalse_WhenCommentNotSaved() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(mockConnection);

        Comment comment = new Comment(UUID.randomUUID().toString(), "user", "body", new Timestamp(System.currentTimeMillis()));
        boolean result = comment.commit();

        assertFalse("Should return false when comment is not saved", result);
    }
}

//BEGIN: Work/DemoTestCreator/2025-05-23__12-45-03.840__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

}

//END: Work/DemoTestCreator/2025-05-23__12-45-03.840__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
