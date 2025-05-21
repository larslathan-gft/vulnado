import com.scalesec.vulnado.LoginController;
import com.scalesec.vulnado.LoginRequest;
import com.scalesec.vulnado.LoginResponse;
import com.scalesec.vulnado.Unauthorized;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private User user;

    @Mock
    private Postgres postgres;

    @Value("${app.secret}")
    private String secret;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void login_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.username = "validUser";
        request.password = "validPassword";

        when(User.fetch(request.username)).thenReturn(user);
        when(Postgres.md5(request.password)).thenReturn("hashedPassword");
        when(user.hashedPassword).thenReturn("hashedPassword");
        when(user.token(secret)).thenReturn("validToken");

        // Act
        LoginResponse response = loginController.login(request);

        // Assert
        assertEquals("validToken", response.token, "Token should match the expected value");
    }

    @Test
    public void login_ShouldThrowUnauthorized_WhenCredentialsAreInvalid() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.username = "invalidUser";
        request.password = "invalidPassword";

        when(User.fetch(request.username)).thenReturn(user);
        when(Postgres.md5(request.password)).thenReturn("wrongHashedPassword");
        when(user.hashedPassword).thenReturn("hashedPassword");

        // Act & Assert
        assertThrows(Unauthorized.class, () -> loginController.login(request), "Should throw Unauthorized exception");
    }

    @Test
    public void login_ShouldThrowUnauthorized_WhenUserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.username = "nonExistentUser";
        request.password = "somePassword";

        when(User.fetch(request.username)).thenReturn(null);

        // Act & Assert
        assertThrows(Unauthorized.class, () -> loginController.login(request), "Should throw Unauthorized exception");
    }
}
