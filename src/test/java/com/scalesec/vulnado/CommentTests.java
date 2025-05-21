package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTests {

    @Test
    public void create_ShouldReturnComment() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment expectedComment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);

        Comment commentSpy = Mockito.spy(expectedComment);
        doReturn(true).when(commentSpy).commit();

        // Act
        Comment result = Comment.create(username, body);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.username);
        assertEquals(body, result.body);
    }

    @Test(expected = BadRequest.class)
    public void create_ShouldThrowBadRequest_WhenCommitFails() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment expectedComment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);

        Comment commentSpy = Mockito.spy(expectedComment);
        doReturn(false).when(commentSpy).commit();

        // Act
        Comment.create(username, body);
    }

    @Test(expected = ServerError.class)
    public void create_ShouldThrowServerError_WhenExceptionOccurs() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment expectedComment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);

        Comment commentSpy = Mockito.spy(expectedComment);
        doThrow(new SQLException("Database error")).when(commentSpy).commit();

        // Act
        Comment.create(username, body);
    }

    @Test
    public void fetch_all_ShouldReturnComments() throws SQLException {
        // Arrange
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment(UUID.randomUUID().toString(), "user1", "body1", new Timestamp(new Date().getTime())));
        expectedComments.add(new Comment(UUID.randomUUID().toString(), "user2", "body2", new Timestamp(new Date().getTime())));

        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("id")).thenReturn(expectedComments.get(0).id, expectedComments.get(1).id);
        when(mockResultSet.getString("username")).thenReturn(expectedComments.get(0).username, expectedComments.get(1).username);
        when(mockResultSet.getString("body")).thenReturn(expectedComments.get(0).body, expectedComments.get(1).body);
        when(mockResultSet.getTimestamp("created_on")).thenReturn(expectedComments.get(0).created_on, expectedComments.get(1).created_on);

        Postgres.setConnection(mockConnection);

        // Act
        List<Comment> result = Comment.fetch_all();

        // Assert
        assertEquals(expectedComments.size(), result.size());
        for (int i = 0; i < expectedComments.size(); i++) {
            assertEquals(expectedComments.get(i).id, result.get(i).id);
            assertEquals(expectedComments.get(i).username, result.get(i).username);
            assertEquals(expectedComments.get(i).body, result.get(i).body);
            assertEquals(expectedComments.get(i).created_on, result.get(i).created_on);
        }
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentDeleted() throws SQLException {
        // Arrange
        String commentId = UUID.randomUUID().toString();

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(mockConnection);

        // Act
        boolean result = Comment.delete(commentId);

        // Assert
        assertTrue(result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentNotDeleted() throws SQLException {
        // Arrange
        String commentId = UUID.randomUUID().toString();

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(mockConnection);

        // Act
        boolean result = Comment.delete(commentId);

        // Assert
        assertFalse(result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenExceptionOccurs() throws SQLException {
        // Arrange
        String commentId = UUID.randomUUID().toString();

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        Postgres.setConnection(mockConnection);

        // Act
        boolean result = Comment.delete(commentId);

        // Assert
        assertFalse(result);
    }
}
