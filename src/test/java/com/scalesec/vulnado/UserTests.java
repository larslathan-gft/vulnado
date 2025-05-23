package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void user_Token_ShouldGenerateValidToken() {
        String secret = "mySecretKey";
        User user = new User("1", "testUser", "hashedPassword");
        String token = user.token(secret);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        JwtParser parser = Jwts.parser().setSigningKey(key);
        assertNotNull("Token should not be null", token);
        assertEquals("Token should contain the correct username", "testUser", parser.parseClaimsJws(token).getBody().getSubject());
    }

    @Test
    public void user_AssertAuth_ShouldNotThrowExceptionForValidToken() {
        String secret = "mySecretKey";
        User user = new User("1", "testUser", "hashedPassword");
        String token = user.token(secret);

        try {
            User.assertAuth(secret, token);
        } catch (Unauthorized e) {
            fail("Should not throw Unauthorized exception for valid token");
        }
    }

    @Test
    public void user_AssertAuth_ShouldThrowExceptionForInvalidToken() {
        String secret = "mySecretKey";
        String invalidToken = "invalidToken";

        try {
            User.assertAuth(secret, invalidToken);
            fail("Should throw Unauthorized exception for invalid token");
        } catch (Unauthorized e) {
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }

    @Test
    public void user_Fetch_ShouldReturnUserForValidUsername() throws Exception {
        String username = "testUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("password")).thenReturn("hashedPassword");

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        User user = User.fetch(username);

        assertNotNull("User should not be null", user);
        assertEquals("User ID should match", "1", user.id);
        assertEquals("Username should match", "testUser", user.username);
        assertEquals("Password should match", "hashedPassword", user.hashedPassword);
    }

    @Test
    public void user_Fetch_ShouldReturnNullForInvalidUsername() throws Exception {
        String username = "invalidUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres mockPostgres = mock(Postgres.class);
        when(mockPostgres.connection()).thenReturn(mockConnection);

        User user = User.fetch(username);

        assertNull("User should be null for invalid username", user);
    }
}
