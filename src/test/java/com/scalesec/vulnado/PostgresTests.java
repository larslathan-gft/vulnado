import com.scalesec.vulnado.Postgres;
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

    @Test
    public void connection_ShouldReturnValidConnection() {
        try {
            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should not be closed", connection.isClosed());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldSetupDatabase() {
        try {
            Postgres.setup();
            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            assertNotNull("Statement should not be null", stmt);
            stmt.executeQuery("SELECT * FROM users");
            stmt.executeQuery("SELECT * FROM comments");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6";
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUser() {
        try {
            String username = "testuser";
            String password = "testpassword";
            Postgres.insertUser(username, password);
            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            assertNotNull("Statement should not be null", stmt);
            stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertComment() {
        try {
            String username = "testuser";
            String body = "This is a test comment";
            Postgres.insertComment(username, body);
            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            assertNotNull("Statement should not be null", stmt);
            stmt.executeQuery("SELECT * FROM comments WHERE username = '" + username + "' AND body = '" + body + "'");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
