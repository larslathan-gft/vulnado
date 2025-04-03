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

    // Test for Comment.create()
    @Test
    public void create_ValidComment_ShouldReturnComment() throws Exception {
        String username = "testUser";
        String body = "This is a test comment";

        // Mocking Postgres.connection() and PreparedStatement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Comment comment = Comment.create(username, body);

        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", username, comment.username);
        assertEquals("Body should match", body, comment.body);
        assertNotNull("Timestamp should not be null", comment.created_on);
    }

    @Test(expected = BadRequest.class)
    public void create_InvalidComment_ShouldThrowBadRequest() throws Exception {
        String username = "testUser";
        String body = "This is a test comment";

        // Mocking Postgres.connection() and PreparedStatement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Comment.create(username, body);
    }

    @Test(expected = ServerError.class)
    public void create_Exception_ShouldThrowServerError() throws Exception {
        String username = "testUser";
        String body = "This is a test comment";

        // Mocking Postgres.connection() to throw SQLException
        when(Postgres.connection()).thenThrow(new SQLException("Database error"));

        Comment.create(username, body);
    }

    // Test for Comment.fetch_all()
    @Test
    public void fetchAll_ValidComments_ShouldReturnList() throws Exception {
        // Mocking Postgres.connection() and ResultSet
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Mocking ResultSet behavior
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("This is a test comment");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = Comment.fetch_all();

        assertNotNull("Comments list should not be null", comments);
        assertEquals("Comments list size should match", 2, comments.size());
    }

    @Test
    public void fetchAll_NoComments_ShouldReturnEmptyList() throws Exception {
        // Mocking Postgres.connection() and ResultSet
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Mocking ResultSet behavior
        when(mockResultSet.next()).thenReturn(false);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull("Comments list should not be null", comments);
        assertTrue("Comments list should be empty", comments.isEmpty());
    }

    // Test for Comment.delete()
    @Test
    public void delete_ValidId_ShouldReturnTrue() throws Exception {
        String id = UUID.randomUUID().toString();

        // Mocking Postgres.connection() and PreparedStatement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = Comment.delete(id);

        assertTrue("Delete should return true for valid ID", result);
    }

    @Test
    public void delete_InvalidId_ShouldReturnFalse() throws Exception {
        String id = UUID.randomUUID().toString();

        // Mocking Postgres.connection() and PreparedStatement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = Comment.delete(id);

        assertFalse("Delete should return false for invalid ID", result);
    }

    @Test
    public void delete_Exception_ShouldReturnFalse() throws Exception {
        String id = UUID.randomUUID().toString();

        // Mocking Postgres.connection() to throw SQLException
        when(Postgres.connection()).thenThrow(new SQLException("Database error"));

        boolean result = Comment.delete(id);

        assertFalse("Delete should return false when exception occurs", result);
    }
}

