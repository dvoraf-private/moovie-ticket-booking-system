package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dtos.LoginUserDto;
import com.example.movieticketbookingsystem.dtos.RegisterUserDto;
import com.example.movieticketbookingsystem.model.User;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.constants.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create instance of the service under test
        authenticationService = new AuthenticationService(userRepository, authenticationManager, passwordEncoder);
    }

    @Test
    void testSignup_UserAlreadyExists() {
        // Given
        // Create the RegisterUserDto instance using the constructor
        RegisterUserDto registerUserDto = new RegisterUserDto(
                "test@example.com",
                "password",
                "Test User",
                AppConstants.Role.ADMIN
        );

        // Mock userRepository.existsByEmail to return true (user already exists)
        when(userRepository.existsByEmail(registerUserDto.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.signup(registerUserDto);
        });
        assertEquals("User with this email already exists", exception.getMessage());

        // Verify the repository was called
        verify(userRepository, times(1)).existsByEmail(registerUserDto.getEmail());
    }

    @Test
    void testSignup_Success() {
        // Given
        // Create the RegisterUserDto instance using the constructor
        RegisterUserDto registerUserDto = new RegisterUserDto(
                "test@example.com",
                "password",
                "Test User",
                AppConstants.Role.ADMIN
        );

        // Mock userRepository.existsByEmail to return false (user doesn't exist)
        when(userRepository.existsByEmail(registerUserDto.getEmail())).thenReturn(false);

        // Mock passwordEncoder.encode to return a mocked password
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");

        // Mock userRepository.save to return the saved user
        User user = new User();
        user.setFullName(registerUserDto.getFullName());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword("encodedPassword");
        user.setRole(registerUserDto.getRole());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = authenticationService.signup(registerUserDto);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        // Verify the repository methods were called
        verify(userRepository, times(1)).existsByEmail(registerUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAuthenticate_Success() {
        // Given
        LoginUserDto loginUserDto = new LoginUserDto("test@example.com", "password");

        // Mock authenticationManager.authenticate to return a successful Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock userRepository.findByEmail to return a mocked user
        User user = new User();
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // When
        User result = authenticationService.authenticate(loginUserDto);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());

        // Verify the authenticationManager.authenticate and userRepository.findByEmail were called
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testAuthenticate_Failure() {
        // Given
        LoginUserDto loginUserDto = new LoginUserDto("invalid@example.com", "password");

        // Mock authenticationManager.authenticate to throw an exception (authentication failure)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(loginUserDto);
        });

        assertEquals("Authentication failed", exception.getMessage());

        // Verify that authenticationManager.authenticate was called
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
