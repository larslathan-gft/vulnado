package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";

    @Test
    public void user_Token_ShouldGenerateValidToken() {
        User user = new User("1", "testuser", "hashedpassword");
        String token = user.token(SECRET_KEY);

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();

        assertEquals("Token should contain the correct username", "testuser", subject);
    }

    @Test
    public void user_AssertAuth_ShouldNotThrowExceptionForValidToken() {
        User user = new User("1", "testuser", "hashedpassword");
        String token = user.token(SECRET_KEY);

        try {
            User.assertAuth(SECRET_KEY, token);
        } catch (Exception e) {
            fail("Exception should not be thrown for a valid token");
        }
    }

    @Test(expected = Unauthorized.class)
    public void user_AssertAuth_ShouldThrowExceptionForInvalidToken() {
        User.assertAuth(SECRET_KEY, "invalidtoken");
    }

    @Test
    public void user_Fetch_ShouldReturnUserForValidUsername() throws Exception {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("password")).thenReturn("hashedpassword");

        Postgres.setConnection(mockConnection);

        User user = User.fetch("testuser");

        assertNotNull("User should not be null for a valid username", user);
        assertEquals("User ID should match", "1", user.id);
        assertEquals("Username should match", "testuser", user.username);
        assertEquals("Password should match", "hashedpassword", user.hashedPassword);
    }

    @Test
    public void user_Fetch_ShouldReturnNullForInvalidUsername() throws Exception {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setConnection(mockConnection);

        User user = User.fetch("invaliduser");

        assertNull("User should be null for an invalid username", user);
    }
}
