import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @Mock
    private User user;

    @Mock
    private Postgres postgres;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @Value("${app.secret}")
    private String secret;

    @org.junit.Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        String username = "validUser";
        String password = "validPassword";
        String hashedPassword = "hashedPassword";
        String token = "validToken";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = username;
        loginRequest.password = password;

        when(User.fetch(username)).thenReturn(user);
        when(Postgres.md5(password)).thenReturn(hashedPassword);
        when(user.hashedPassword).thenReturn(hashedPassword);
        when(user.token(secret)).thenReturn(token);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        String username = "invalidUser";
        String password = "invalidPassword";
        String hashedPassword = "hashedPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = username;
        loginRequest.password = password;

        when(User.fetch(username)).thenReturn(user);
        when(Postgres.md5(password)).thenReturn("wrongHashedPassword");
        when(user.hashedPassword).thenReturn(hashedPassword);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized());
    }
}
