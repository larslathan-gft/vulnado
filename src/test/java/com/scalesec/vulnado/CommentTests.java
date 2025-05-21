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

    // Helper method to create a mock connection
    private Connection createMockConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        return connection;
    }

    // Helper method to create a mock result set
    private ResultSet createMockResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSet.getString("username")).thenReturn("testUser");
        when(resultSet.getString("body")).thenReturn("testBody");
        when(resultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));
        return resultSet;
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
        assertNotNull(comment);
        assertEquals(username, comment.username);
        assertEquals(body, comment.body);
    }

    @Test(expected = ServerError.class)
    public void create_ShouldThrowServerError() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());
        Postgres.setConnection(mockConnection);

        // Act
        Comment.create(username, body);
    }

    @Test
    public void fetch_all_ShouldReturnComments() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = createMockResultSet();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        Postgres.setConnection(mockConnection);

        // Act
        List<Comment> comments = Comment.fetch_all();

        // Assert
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("testUser", comments.get(0).username);
        assertEquals("testBody", comments.get(0).body);
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
        assertTrue(result);
    }

    @Test
    public void delete_ShouldReturnFalse() throws SQLException {
        // Arrange
        String id = UUID.randomUUID().toString();
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());
        Postgres.setConnection(mockConnection);

        // Act
        Boolean result = Comment.delete(id);

        // Assert
        assertFalse(result);
    }
}
