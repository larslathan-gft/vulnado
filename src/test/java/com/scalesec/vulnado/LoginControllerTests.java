package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTests {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private User mockUser;

    @Mock
    private Postgres mockPostgres;

    @Value("${app.secret}")
    private String secret = "testSecret";

    // Helper method to create a LoginRequest
    private LoginRequest createLoginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = password;
        return request;
    }

    // Helper method to create a User
    private User createUser(String username, String hashedPassword) {
        User user = new User();
        user.username = username;
        user.hashedPassword = hashedPassword;
        return user;
    }

    @Test
    public void login_ValidCredentials_ShouldReturnToken() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String hashedPassword = "hashedPassword";
        String expectedToken = "generatedToken";

        LoginRequest request = createLoginRequest(username, password);
        User user = createUser(username, hashedPassword);

        when(User.fetch(username)).thenReturn(user);
        when(Postgres.md5(password)).thenReturn(hashedPassword);
        when(user.token(secret)).thenReturn(expectedToken);

        // Act
        LoginResponse response = loginController.login(request);

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Token should match the expected value", expectedToken, response.token);

        // Verify
        verify(User.fetch(username), times(1));
        verify(Postgres.md5(password), times(1));
        verify(user.token(secret), times(1));
    }

    @Test(expected = Unauthorized.class)
    public void login_InvalidCredentials_ShouldThrowUnauthorized() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        String hashedPassword = "hashedPassword";

        LoginRequest request = createLoginRequest(username, password);
        User user = createUser(username, hashedPassword);

        when(User.fetch(username)).thenReturn(user);
        when(Postgres.md5(password)).thenReturn("wrongHashedPassword");

        // Act
        loginController.login(request);

        // Assert
        // Exception is expected
    }

    @Test(expected = Unauthorized.class)
    public void login_NonExistentUser_ShouldThrowUnauthorized() {
        // Arrange
        String username = "nonExistentUser";
        String password = "testPassword";

        LoginRequest request = createLoginRequest(username, password);

        when(User.fetch(username)).thenReturn(null);

        // Act
        loginController.login(request);

        // Assert
        // Exception is expected
    }

    @Test
    public void login_NullRequest_ShouldThrowBadRequest() {
        // Arrange
        LoginRequest request = null;

        try {
            // Act
            loginController.login(request);
            fail("Expected ResponseStatusException to be thrown");
        } catch (ResponseStatusException ex) {
            // Assert
            assertEquals("Should return BAD_REQUEST status", HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }
}
