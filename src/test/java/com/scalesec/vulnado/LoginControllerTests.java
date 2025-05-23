import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
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

    private MockMvc mockMvc;

    @Test
    public void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        // Arrange
        String username = "validUser";
        String password = "validPassword";
        String hashedPassword = "hashedPassword";
        String token = "validToken";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = username;
        loginRequest.password = password;

        when(User.fetch(anyString())).thenReturn(user);
        when(user.getHashedPassword()).thenReturn(hashedPassword);
        when(Postgres.md5(anyString())).thenReturn(hashedPassword);
        when(user.token(anyString())).thenReturn(token);

        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        // Arrange
        String username = "invalidUser";
        String password = "invalidPassword";
        String hashedPassword = "hashedPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = username;
        loginRequest.password = password;

        when(User.fetch(anyString())).thenReturn(user);
        when(user.getHashedPassword()).thenReturn(hashedPassword);
        when(Postgres.md5(anyString())).thenReturn("wrongHashedPassword");

        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized());
    }
}
