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

    private static final String SECRET = "mysecretkeymysecretkeymysecretkeymysecretkey";

    // Helper method to create a mock ResultSet
    private ResultSet createMockResultSet(String userId, String username, String password) throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getString("user_id")).thenReturn(userId);
        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        return rs;
    }

    @Test
    public void token_ShouldGenerateValidToken() {
        User user = new User("1", "testuser", "hashedpassword");
        String token = user.token(SECRET);
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        assertEquals("Token should contain the correct username", "testuser", subject);
    }

    @Test
    public void assertAuth_ShouldNotThrowExceptionForValidToken() {
        User user = new User("1", "testuser", "hashedpassword");
        String token = user.token(SECRET);
        User.assertAuth(SECRET, token);
    }

    @Test(expected = Unauthorized.class)
    public void assertAuth_ShouldThrowExceptionForInvalidToken() {
        User.assertAuth(SECRET, "invalidtoken");
    }

    @Test
    public void fetch_ShouldReturnUserForValidUsername() throws Exception {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = createMockResultSet("1", "testuser", "hashedpassword");

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        Postgres.setConnection(mockConnection); // Assuming Postgres class has a method to set the connection

        User user = User.fetch("testuser");
        assertNotNull("User should not be null", user);
        assertEquals("User ID should match", "1", user.id);
        assertEquals("Username should match", "testuser", user.username);
        assertEquals("Password should match", "hashedpassword", user.hashedPassword);
    }

    @Test
    public void fetch_ShouldReturnNullForInvalidUsername() throws Exception {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(false);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        Postgres.setConnection(mockConnection); // Assuming Postgres class has a method to set the connection

        User user = User.fetch("invaliduser");
        assertNull("User should be null", user);
    }
}
