package com.movieticketbookingsystem.controller;

import com.movieticketbookingsystem.constants.AppConstants;
import com.movieticketbookingsystem.constants.LoginResponse;
import com.movieticketbookingsystem.dtos.LoginUserDto;
import com.movieticketbookingsystem.dtos.RegisterUserDto;
import com.movieticketbookingsystem.entity.User;
import com.movieticketbookingsystem.service.AuthenticationService;
import com.movieticketbookingsystem.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    private RegisterUserDto registerUserDto;
    private User user;

    private LoginUserDto loginUserDto;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Initialize mock data
        registerUserDto = new RegisterUserDto("test@example.com", "password", "Test User", AppConstants.Role.ADMIN);
        user = new User();
        user.setId(111);
        user.setEmail("test@example.com");
        user.setFullName("Test User");

        loginUserDto = new LoginUserDto("test@example.com", "password123");

        user = new User();
        user.setId(111);
        user.setEmail("test@example.com");
        user.setFullName("Test User");

        token = "mock-jwt-token";
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        when(authenticationService.signup(registerUserDto)).thenReturn(user);

        // Act
        ResponseEntity<?> response = authController.register(registerUserDto, mock(BindingResult.class));

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        verify(authenticationService, times(1)).signup(registerUserDto);
    }

    @Test
    void testRegisterValidationFailure() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError error = new ObjectError("email", "Email is required");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(error));

        // Act
        ResponseEntity<?> response = authController.register(registerUserDto, bindingResult);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((List<String>) response.getBody()).contains("Email is required"));
    }

    @Test
    void testRegisterErrorHandling() {
        // Arrange
        when(authenticationService.signup(registerUserDto)).thenThrow(new RuntimeException("Error during registration"));

        // Act
        ResponseEntity<?> response = authController.register(registerUserDto ,mock(BindingResult.class));

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error during registration"));
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        when(authenticationService.authenticate(loginUserDto)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        // Act
        ResponseEntity<?> response = authController.authenticate(loginUserDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        LoginResponse body = (LoginResponse) response.getBody(); // Cast response body to LoginResponse
        assertNotNull(body);
        assertEquals(token, body.getToken());
        assertEquals(3600, body.getExpiresIn());
        verify(authenticationService, times(1)).authenticate(loginUserDto);
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void testLoginInvalidCredentials() {
        // Arrange
        when(authenticationService.authenticate(loginUserDto)).thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = authController.authenticate(loginUserDto);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid credentials"));
    }
}