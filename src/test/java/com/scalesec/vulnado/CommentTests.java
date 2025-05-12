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
    public void create_ShouldReturnComment() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
        Comment spyComment = Mockito.spy(comment);
        doReturn(true).when(spyComment).commit();

        // Act
        Comment result = Comment.create(username, body);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.username);
        assertEquals(body, result.body);
        assertNotNull(result.id);
        assertNotNull(result.created_on);
    }

    @Test(expected = BadRequest.class)
    public void create_ShouldThrowBadRequest() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
        Comment spyComment = Mockito.spy(comment);
        doReturn(false).when(spyComment).commit();

        // Act
        Comment.create(username, body);
    }

    @Test(expected = ServerError.class)
    public void create_ShouldThrowServerError() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
        Comment spyComment = Mockito.spy(comment);
        doThrow(new SQLException("Database error")).when(spyComment).commit();

        // Act
        Comment.create(username, body);
    }

    @Test
    public void fetch_all_ShouldReturnComments() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("select * from comments;")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("id")).thenReturn("testId");
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("testBody");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(new Date().getTime()));

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        // Act
        List<Comment> comments = Comment.fetch_all();

        // Assert
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("testId", comments.get(0).id);
        assertEquals("testUser", comments.get(0).username);
        assertEquals("testBody", comments.get(0).body);
        assertNotNull(comments.get(0).created_on);
    }

    @Test
    public void delete_ShouldReturnTrue() throws SQLException {
        // Arrange
        String id = "testId";
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement("DELETE FROM comments where id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        // Act
        boolean result = Comment.delete(id);

        // Assert
        assertTrue(result);
    }

    @Test
    public void delete_ShouldReturnFalse() throws SQLException {
        // Arrange
        String id = "testId";
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement("DELETE FROM comments where id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        // Act
        boolean result = Comment.delete(id);

        // Assert
        assertFalse(result);
    }

    @Test
    public void commit_ShouldReturnTrue() throws SQLException {
        // Arrange
        String id = "testId";
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment comment = new Comment(id, username, body, timestamp);

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement("INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        // Act
        boolean result = comment.commit();

        // Assert
        assertTrue(result);
    }

    @Test
    public void commit_ShouldReturnFalse() throws SQLException {
        // Arrange
        String id = "testId";
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment comment = new Comment(id, username, body, timestamp);

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement("INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        // Act
        boolean result = comment.commit();

        // Assert
        assertFalse(result);
    }
}
