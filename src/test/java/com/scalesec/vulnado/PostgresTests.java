package com.scalesec.vulnado;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    private Connection mockConnection;
    private Statement mockStatement;
    private PreparedStatement mockPreparedStatement;

    @Before
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void connection_ShouldReturnValidConnection() throws Exception {
        // Mock DriverManager to return the mock connection
        DriverManager.setLogWriter(null);
        DriverManager.setLoginTimeout(0);
        DriverManager.registerDriver(new org.postgresql.Driver());
        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

        Connection connection = Postgres.connection();
        assertNotNull("Connection should not be null", connection);
    }

    @Test
    public void setup_ShouldSetupDatabase() throws Exception {
        // Mock the connection method to return the mock connection
        Postgres mockPostgres = Mockito.spy(new Postgres());
        doReturn(mockConnection).when(mockPostgres).connection();

        mockPostgres.setup();

        verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
        verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");
        verify(mockStatement, times(1)).executeUpdate("DELETE FROM users");
        verify(mockStatement, times(1)).executeUpdate("DELETE FROM comments");
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6";

        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUser() throws Exception {
        // Mock the connection method to return the mock connection
        Postgres mockPostgres = Mockito.spy(new Postgres());
        doReturn(mockConnection).when(mockPostgres).connection();

        mockPostgres.insertUser("testuser", "testpassword");

        verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
        verify(mockPreparedStatement, times(1)).setString(eq(2), eq("testuser"));
        verify(mockPreparedStatement, times(1)).setString(eq(3), eq(Postgres.md5("testpassword")));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void insertComment_ShouldInsertComment() throws Exception {
        // Mock the connection method to return the mock connection
        Postgres mockPostgres = Mockito.spy(new Postgres());
        doReturn(mockConnection).when(mockPostgres).connection();

        mockPostgres.insertComment("testuser", "testcomment");

        verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
        verify(mockPreparedStatement, times(1)).setString(eq(2), eq("testuser"));
        verify(mockPreparedStatement, times(1)).setString(eq(3), eq("testcomment"));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
