package com.example.movieticketbookingsystem.config;
import com.example.movieticketbookingsystem.exception.CustomAccessDeniedHandler;
import com.example.movieticketbookingsystem.exception.CustomAuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/**").permitAll()  // Allow unauthenticated access to authentication endpoints
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()  // Allow unauthenticated access to Swagger
                        .requestMatchers("api/movies/**", "api/showtimes/**").hasRole("ADMIN") // Only ADMIN can manage movies and showtimes
                        .requestMatchers("api/tickets/**").hasRole("CUSTOMER") // Only CUSTOMER can book tickets
                        .anyRequest().authenticated()  // All other endpoints require authentication
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before the username/password filter
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            // Custom response for 401 Unauthorized
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication is required to access this resource.");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            // Custom response for 403 Forbidden
//                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
//                        })
//                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}