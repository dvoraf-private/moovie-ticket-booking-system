package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.constants.AppConstants;
import com.example.movieticketbookingsystem.constants.LoginResponse;
import com.example.movieticketbookingsystem.dtos.LoginUserDto;
import com.example.movieticketbookingsystem.dtos.RegisterUserDto;
import com.example.movieticketbookingsystem.model.User;
import com.example.movieticketbookingsystem.service.AuthenticationService;
import com.example.movieticketbookingsystem.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        System.out.println("im here before");

        // Act
        ResponseEntity<?> response = authController.register(registerUserDto ,mock(BindingResult.class));

        System.out.println("im here: " + response.getBody().toString());
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

//package com.example.movieticketbookingsystem.controller;
//
//import com.example.movieticketbookingsystem.constants.AppConstants;
//import com.example.movieticketbookingsystem.constants.LoginResponse;
//import com.example.movieticketbookingsystem.dtos.LoginUserDto;
//import com.example.movieticketbookingsystem.dtos.RegisterUserDto;
//import com.example.movieticketbookingsystem.model.User;
//import com.example.movieticketbookingsystem.service.AuthenticationService;
//import com.example.movieticketbookingsystem.service.JwtService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class AuthControllerTest {
//
//    @Mock
//    private AuthenticationService authenticationService;
//
//    @Mock
//    private JwtService jwtService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    private RegisterUserDto registerUserDto;
//    private LoginUserDto loginUserDto;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Initialize mocks
//
//        // Sample data for testing
//        user = new User();
//        user.setId(1L);
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//
//        registerUserDto = new RegisterUserDto("test@example.com", "password", "testUser", AppConstants.Role.ADMIN);
//
//
//        loginUserDto = new LoginUserDto("test@example.com", "password");
//    }
//
//    @Test
//    void testRegisterSuccess() {
//        // Arrange
//        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(user);
//
//        // Act
//        ResponseEntity<User> response = authController.register(registerUserDto);
//
//        // Assert
//        assertEquals(200, response.getStatusCodeValue()); // HTTP 200 OK
//        assertNotNull(response.getBody());
//        assertEquals(user.getUsername(), response.getBody().getUsername());
//        verify(authenticationService, times(1)).signup(any(RegisterUserDto.class)); // Verify signup was called once
//    }
//
//    @Test
//    void testRegisterFailure() {
//        // Arrange
//        when(authenticationService.signup(any(RegisterUserDto.class))).thenThrow(new RuntimeException("Registration failed"));
//
//        // Act & Assert
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            authController.register(registerUserDto);
//        });
//
//        assertEquals("Registration failed", exception.getMessage());
//        verify(authenticationService, times(1)).signup(any(RegisterUserDto.class)); // Ensure signup was called
//    }
//
//    @Test
//    void testLoginSuccess() {
//        // Arrange
//        String jwtToken = "mocked_jwt_token";
//        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(user);
//        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
//        when(jwtService.getExpirationTime()).thenReturn(3600L); // Expiration time in seconds
//
//        // Act
//        ResponseEntity<LoginResponse> response = authController.authenticate(loginUserDto);
//
//        // Assert
//        assertEquals(200, response.getStatusCodeValue()); // HTTP 200 OK
//        assertNotNull(response.getBody());
//        assertEquals(jwtToken, response.getBody().getToken());
//        assertEquals(3600L, response.getBody().getExpiresIn());
//        verify(authenticationService, times(1)).authenticate(any(LoginUserDto.class)); // Ensure authenticate was called
//        verify(jwtService, times(1)).generateToken(any(User.class)); // Ensure JWT was generated
//    }
//
//    @Test
//    void testLoginFailure() {
//        // Arrange
//        when(authenticationService.authenticate(any(LoginUserDto.class))).thenThrow(new RuntimeException("Invalid credentials"));
//
//        // Act & Assert
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            authController.authenticate(loginUserDto);
//        });
//
//        assertEquals("Invalid credentials", exception.getMessage());
//        verify(authenticationService, times(1)).authenticate(any(LoginUserDto.class)); // Ensure authenticate was called
//    }
//}
