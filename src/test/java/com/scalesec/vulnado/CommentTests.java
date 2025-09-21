package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTests {

    // Helper method to create a mock connection
    private Connection createMockConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        return mockConnection;
    }

    // Helper method to create a mock result set
    private ResultSet createMockResultSet() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, false); // Simulate one row
        when(mockResultSet.getString("id")).thenReturn("test-id");
        when(mockResultSet.getString("username")).thenReturn("test-user");
        when(mockResultSet.getString("body")).thenReturn("test-body");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));
        return mockResultSet;
    }

    @Test
    public void create_ShouldReturnComment_WhenCommitSucceeds() throws SQLException {
        // Arrange
        Connection mockConnection = createMockConnection();
        Postgres.setMockConnection(mockConnection);

        // Act
        Comment comment = Comment.create("test-user", "test-body");

        // Assert
        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", "test-user", comment.username);
        assertEquals("Body should match", "test-body", comment.body);
    }

    @Test(expected = BadRequest.class)
    public void create_ShouldThrowBadRequest_WhenCommitFails() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate failure
        Postgres.setMockConnection(mockConnection);

        // Act
        Comment.create("test-user", "test-body");
    }

    @Test
    public void fetch_all_ShouldReturnListOfComments() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = createMockResultSet();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        Postgres.setMockConnection(mockConnection);

        // Act
        List<Comment> comments = Comment.fetch_all();

        // Assert
        assertNotNull("Comments list should not be null", comments);
        assertEquals("Comments list should contain one comment", 1, comments.size());
        assertEquals("Comment username should match", "test-user", comments.get(0).username);
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentDeleted() throws SQLException {
        // Arrange
        Connection mockConnection = createMockConnection();
        Postgres.setMockConnection(mockConnection);

        // Act
        boolean result = Comment.delete("test-id");

        // Assert
        assertTrue("Delete should return true", result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenExceptionOccurs() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Test exception"));
        Postgres.setMockConnection(mockConnection);

        // Act
        boolean result = Comment.delete("test-id");

        // Assert
        assertFalse("Delete should return false", result);
    }
}
