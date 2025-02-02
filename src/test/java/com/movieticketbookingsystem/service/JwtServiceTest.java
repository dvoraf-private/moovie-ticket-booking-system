//package com.movieticketbookingsystem.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JwtServiceTest {
//
//    @Mock
//    private UserDetails userDetails;
//
//    @InjectMocks
//    private JwtService jwtService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGenerateToken() {
//        // Given
//        String username = "test@example.com";
//        String role = "ROLE_USER";
//        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
//        userDetails = new User(username, "password", authorities);
//
//        // When
//        String token = jwtService.generateToken(userDetails);
//
//        // Then
//        assertNotNull(token);
//        assertTrue(token.startsWith("eyJ")); // JWT tokens usually start with 'eyJ'
//    }
//
//    @Test
//    void testExtractUsername() {
//        String username = "test@example.com";
//        String token = generateToken(username);
//
//        String extractedUsername = jwtService.extractUsername(token);
//        assertEquals(username, extractedUsername);
//    }
//
//    @Test
//    void testExtractClaim() {
//        String username = "test@example.com";
//        String token = generateToken(username);
//
//        String extractedSubject = jwtService.extractClaim(token, Claims::getSubject);
//
//        assertEquals(username, extractedSubject);
//    }
//
//    @Test
//    void testIsTokenValid() {
//        String username = "test@example.com";
//        String token = generateToken(username);
//
//        // Mock userDetails
//        when(userDetails.getUsername()).thenReturn(username);
//
//        boolean isValid = jwtService.isTokenValid(token, userDetails);
//
//        assertTrue(isValid);
//    }
//
//    @Test
//    void testIsTokenExpired() {
//        String username = "test@example.com";
//        String token = generateToken(username);
//
//        boolean isExpired = jwtService.isTokenExpired(token);
//
//        assertFalse(isExpired);  // Since the token was just created, it should not be expired
//    }
//
//    @Test
//    void testIsTokenExpired_ExpiredToken() {
//        String username = "test@example.com";
//        String expiredToken = generateExpiredToken(username);
//
//        boolean isExpired = jwtService.isTokenExpired(expiredToken);
//
//        assertTrue(isExpired);  // This should return true since the token is expired
//    }
//
//    private String generateToken(String username) {
//        // Simulate JWT generation
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 10000))  // Expires in 10 seconds
//                .signWith(SignatureAlgorithm.HS256, "secret")
//                .compact();
//    }
//
//    private String generateExpiredToken(String username) {
//        // Simulate an expired JWT
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))  // Issued 20 seconds ago
//                .setExpiration(new Date(System.currentTimeMillis() - 10000))  // Expired 10 seconds ago
//                .signWith(SignatureAlgorithm.HS256, "secret")
//                .compact();
//    }
//}
