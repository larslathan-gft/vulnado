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
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @Mock
    private User mockUser;

    @Mock
    private Postgres mockPostgres;

    @InjectMocks
    private LoginController loginController;

    @Value("${app.secret}")
    private String secret;

    @Test
    public void login_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.username = "validUser";
        request.password = "validPassword";

        when(User.fetch(anyString())).thenReturn(mockUser);
        when(mockPostgres.md5(anyString())).thenReturn("hashedPassword");
        when(mockUser.hashedPassword).thenReturn("hashedPassword");
        when(mockUser.token(secret)).thenReturn("validToken");

        // Act
        LoginResponse response = loginController.login(request);

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Token should match", "validToken", response.token);
    }

    @Test(expected = Unauthorized.class)
    public void login_ShouldThrowUnauthorized_WhenCredentialsAreInvalid() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.username = "invalidUser";
        request.password = "invalidPassword";

        when(User.fetch(anyString())).thenReturn(mockUser);
        when(mockPostgres.md5(anyString())).thenReturn("wrongHashedPassword");
        when(mockUser.hashedPassword).thenReturn("hashedPassword");

        // Act
        loginController.login(request);
    }

    @Test
    public void login_ShouldThrowUnauthorized_WhenUserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.username = "nonExistentUser";
        request.password = "somePassword";

        when(User.fetch(anyString())).thenReturn(null);

        // Act & Assert
        try {
            loginController.login(request);
            fail("Expected Unauthorized exception to be thrown");
        } catch (Unauthorized e) {
            assertEquals("Access Denied", e.getMessage());
        }
    }
}
