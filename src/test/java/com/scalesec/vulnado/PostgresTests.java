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
            connection.close();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldSetupDatabaseWithoutErrors() {
        try {
            Postgres.setup();
        } catch (Exception e) {
            fail("Exception should not be thrown during setup: " + e.getMessage());
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
    public void insertUser_ShouldInsertUserWithoutErrors() {
        try {
            Postgres.insertUser("testuser", "testpassword");
        } catch (Exception e) {
            fail("Exception should not be thrown during user insertion: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentWithoutErrors() {
        try {
            Postgres.insertComment("testuser", "This is a test comment");
        } catch (Exception e) {
            fail("Exception should not be thrown during comment insertion: " + e.getMessage());
        }
    }

    // Mocking the connection method to test insertUser and insertComment methods
    private Connection mockConnection() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        return mockConnection;
    }

    @Test
    public void insertUser_ShouldCallPreparedStatement() {
        try {
            Connection mockConnection = mockConnection();
            Postgres.insertUser("mockuser", "mockpassword");
            verify(mockConnection.prepareStatement(anyString()), times(1)).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown during user insertion with mock: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldCallPreparedStatement() {
        try {
            Connection mockConnection = mockConnection();
            Postgres.insertComment("mockuser", "This is a mock comment");
            verify(mockConnection.prepareStatement(anyString()), times(1)).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown during comment insertion with mock: " + e.getMessage());
        }
    }
}
