package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    // Test for User constructor
    @Test
    public void User_Constructor_ShouldInitializeFieldsCorrectly() {
        String id = "1";
        String username = "testUser";
        String hashedPassword = "hashedPassword123";

        User user = new User(id, username, hashedPassword);

        assertEquals("User ID should be initialized correctly", id, user.id);
        assertEquals("Username should be initialized correctly", username, user.username);
        assertEquals("Hashed password should be initialized correctly", hashedPassword, user.hashedPassword);
    }

    // Test for token generation
    @Test
    public void User_Token_ShouldGenerateValidToken() {
        String secret = "superSecretKey12345678901234567890123456789012";
        User user = new User("1", "testUser", "hashedPassword123");

        String token = user.token(secret);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        String expectedSubject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();

        assertEquals("Token should contain the correct username", user.username, expectedSubject);
    }

    // Test for assertAuth with valid token
    @Test
    public void User_AssertAuth_ShouldNotThrowExceptionForValidToken() {
        String secret = "superSecretKey12345678901234567890123456789012";
        User user = new User("1", "testUser", "hashedPassword123");
        String token = user.token(secret);

        try {
            User.assertAuth(secret, token);
        } catch (Exception e) {
            fail("assertAuth should not throw an exception for a valid token");
        }
    }

    // Test for assertAuth with invalid token
    @Test(expected = Unauthorized.class)
    public void User_AssertAuth_ShouldThrowUnauthorizedForInvalidToken() {
        String secret = "superSecretKey12345678901234567890123456789012";
        String invalidToken = "invalidToken";

        User.assertAuth(secret, invalidToken);
    }

    // Test for fetch method with valid username
    @Test
    public void User_Fetch_ShouldReturnUserForValidUsername() throws Exception {
        String username = "testUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn(username);
        when(mockResultSet.getString("password")).thenReturn("hashedPassword123");

        User user = User.fetch(username);

        assertNotNull("Fetch should return a user object for a valid username", user);
        assertEquals("User ID should match the expected value", "1", user.id);
        assertEquals("Username should match the expected value", username, user.username);
        assertEquals("Password should match the expected value", "hashedPassword123", user.hashedPassword);
    }

    // Test for fetch method with invalid username
    @Test
    public void User_Fetch_ShouldReturnNullForInvalidUsername() throws Exception {
        String username = "invalidUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        User user = User.fetch(username);

        assertNull("Fetch should return null for an invalid username", user);
    }
}

