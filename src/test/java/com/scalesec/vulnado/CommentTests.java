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
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("testBody");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(new Date().getTime()));
        return mockResultSet;
    }

    @Test
    public void create_ShouldReturnComment() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Connection mockConnection = createMockConnection();
        Postgres.setConnection(mockConnection);

        // Act
        Comment comment = Comment.create(username, body);

        // Assert
        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", username, comment.username);
        assertEquals("Body should match", body, comment.body);
    }

    @Test(expected = BadRequest.class)
    public void create_ShouldThrowBadRequest() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Connection mockConnection = createMockConnection();
        when(mockConnection.prepareStatement(anyString()).executeUpdate()).thenReturn(0);
        Postgres.setConnection(mockConnection);

        // Act
        Comment.create(username, body);
    }

    @Test
    public void fetch_all_ShouldReturnComments() throws SQLException {
        // Arrange
        Connection mockConnection = createMockConnection();
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = createMockResultSet();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        Postgres.setConnection(mockConnection);

        // Act
        List<Comment> comments = Comment.fetch_all();

        // Assert
        assertNotNull("Comments list should not be null", comments);
        assertFalse("Comments list should not be empty", comments.isEmpty());
    }

    @Test
    public void delete_ShouldReturnTrue() throws SQLException {
        // Arrange
        String id = UUID.randomUUID().toString();
        Connection mockConnection = createMockConnection();
        Postgres.setConnection(mockConnection);

        // Act
        Boolean result = Comment.delete(id);

        // Assert
        assertTrue("Delete should return true", result);
    }

    @Test
    public void delete_ShouldReturnFalse() throws SQLException {
        // Arrange
        String id = UUID.randomUUID().toString();
        Connection mockConnection = createMockConnection();
        when(mockConnection.prepareStatement(anyString()).executeUpdate()).thenReturn(0);
        Postgres.setConnection(mockConnection);

        // Act
        Boolean result = Comment.delete(id);

        // Assert
        assertFalse("Delete should return false", result);
    }
}
