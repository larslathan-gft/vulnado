package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    // Helper method to mock a database connection
    private Connection mockConnection() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        return mockConnection;
    }

    @Test
    public void connection_ShouldReturnValidConnection() {
        try {
            // Mock environment variables
            System.setProperty("PGHOST", "localhost");
            System.setProperty("PGDATABASE", "testdb");
            System.setProperty("PGUSER", "testuser");
            System.setProperty("PGPASSWORD", "testpassword");

            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertData() {
        try {
            Connection mockConnection = mockConnection();
            Postgres.setup();
            verify(mockConnection.createStatement(), atLeastOnce()).executeUpdate(anyString());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6"; // Precomputed MD5 hash for "test"
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() {
        try {
            Connection mockConnection = mockConnection();
            String username = "testuser";
            String password = "testpassword";

            Postgres.insertUser(username, password);

            verify(mockConnection.prepareStatement(anyString()), atLeastOnce()).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        try {
            Connection mockConnection = mockConnection();
            String username = "testuser";
            String body = "This is a test comment";

            Postgres.insertComment(username, body);

            verify(mockConnection.prepareStatement(anyString()), atLeastOnce()).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
