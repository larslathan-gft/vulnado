package com.scalesec.vulnado;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        LoginRequest request = new LoginRequest();
        request.username = "validUser";
        request.password = "validPassword";

        when(User.fetch(anyString())).thenReturn(user);
        when(postgres.md5(anyString())).thenReturn("hashedPassword");
        when(user.hashedPassword).thenReturn("hashedPassword");
        when(user.token(secret)).thenReturn("validToken");

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"validUser\",\"password\":\"validPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertNotNull("Response should not be null", response);
                    assertEquals("Response should contain token", "{\"token\":\"validToken\"}", response);
                });
    }

    @Test
    public void login_ShouldThrowUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        LoginRequest request = new LoginRequest();
        request.username = "invalidUser";
        request.password = "invalidPassword";

        when(User.fetch(anyString())).thenReturn(user);
        when(postgres.md5(anyString())).thenReturn("wrongHashedPassword");
        when(user.hashedPassword).thenReturn("hashedPassword");

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"invalidUser\",\"password\":\"invalidPassword\"}"))
                .andExpect(status().isUnauthorized());
    }
}

// Existing Tests
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }
}
