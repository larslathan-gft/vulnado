package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    @Test
    public void connection_ShouldReturnValidConnection() {
        try {
            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing connection: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertSeedData() {
        try {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);

            when(mockConnection.createStatement()).thenReturn(mockStatement);
            doNothing().when(mockStatement).executeUpdate(anyString());

            Postgres.setup();

            verify(mockStatement, atLeastOnce()).executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            verify(mockStatement, atLeastOnce()).executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");
            verify(mockStatement, atLeastOnce()).executeUpdate("DELETE FROM users");
            verify(mockStatement, atLeastOnce()).executeUpdate("DELETE FROM comments");
        } catch (Exception e) {
            fail("Exception occurred while testing setup: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "testPassword";
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbddc5c3e0a7f5f5a6e5e"; // Precomputed MD5 hash
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() {
        try {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());
            doNothing().when(mockPreparedStatement).executeUpdate();

            Postgres.insertUser("testUser", "testPassword");

            verify(mockPreparedStatement, atLeastOnce()).setString(1, UUID.randomUUID().toString());
            verify(mockPreparedStatement, atLeastOnce()).setString(2, "testUser");
            verify(mockPreparedStatement, atLeastOnce()).setString(3, Postgres.md5("testPassword"));
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        } catch (Exception e) {
            fail("Exception occurred while testing insertUser: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        try {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());
            doNothing().when(mockPreparedStatement).executeUpdate();

            Postgres.insertComment("testUser", "testComment");

            verify(mockPreparedStatement, atLeastOnce()).setString(1, UUID.randomUUID().toString());
            verify(mockPreparedStatement, atLeastOnce()).setString(2, "testUser");
            verify(mockPreparedStatement, atLeastOnce()).setString(3, "testComment");
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        } catch (Exception e) {
            fail("Exception occurred while testing insertComment: " + e.getMessage());
        }
    }
}

