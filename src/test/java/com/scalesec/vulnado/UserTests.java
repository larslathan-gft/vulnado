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
import java.sql.SQLException;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void userToken_ShouldGenerateValidToken() {
        String secret = "mySecretKey123456789012345678901234567890";
        User user = new User("1", "testUser", "hashedPassword");
        String token = user.token(secret);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        Jws<Claims> claimsJws = parser.parseClaimsJws(token);

        assertEquals("Token should contain the correct username", "testUser", claimsJws.getBody().getSubject());
    }

    @Test
    public void assertAuth_ShouldValidateToken() {
        String secret = "mySecretKey123456789012345678901234567890";
        User user = new User("1", "testUser", "hashedPassword");
        String token = user.token(secret);

        try {
            User.assertAuth(secret, token);
        } catch (Unauthorized e) {
            fail("Token validation should not throw an exception");
        }
    }

    @Test(expected = Unauthorized.class)
    public void assertAuth_ShouldThrowExceptionForInvalidToken() {
        String secret = "mySecretKey123456789012345678901234567890";
        String invalidToken = "invalidToken";

        User.assertAuth(secret, invalidToken);
    }

    @Test
    public void fetch_ShouldReturnUser() throws SQLException {
        String username = "testUser";
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery("select * from users where username = '" + username + "' limit 1")).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getString("user_id")).thenReturn("1");
        Mockito.when(mockResultSet.getString("username")).thenReturn("testUser");
        Mockito.when(mockResultSet.getString("password")).thenReturn("hashedPassword");

        Postgres.setConnection(mockConnection); // Assuming Postgres class has a method to set the connection

        User user = User.fetch(username);

        assertNotNull("User should not be null", user);
        assertEquals("User ID should match", "1", user.id);
        assertEquals("Username should match", "testUser", user.username);
        assertEquals("Password should match", "hashedPassword", user.hashedPassword);
    }

    @Test
    public void fetch_ShouldReturnNullForNonExistentUser() throws SQLException {
        String username = "nonExistentUser";
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery("select * from users where username = '" + username + "' limit 1")).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        Postgres.setConnection(mockConnection); // Assuming Postgres class has a method to set the connection

        User user = User.fetch(username);

        assertNull("User should be null for non-existent username", user);
    }
}
