package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.constants.LoginResponse;
import com.example.movieticketbookingsystem.dtos.LoginUserDto;
import com.example.movieticketbookingsystem.dtos.RegisterUserDto;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.model.User;
import com.example.movieticketbookingsystem.service.AuthenticationService;
import com.example.movieticketbookingsystem.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/auth")
@RestController
public class AuthController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Register a new user",
            description = "This endpoint allows a user to register by providing their details like email, password, full name, and role. If validation fails, a list of error messages will be returned."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation errors or bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public  ResponseEntity<?> register(@RequestBody @Valid RegisterUserDto registerUserDto, BindingResult bindingResult) {

            if (bindingResult.hasErrors()) {
                // Collect error messages and return a 400 response
                List<String> errorMessages = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
            }
        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during registration: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Login an existing user",
            description = "This endpoint authenticates a user and returns a JWT token for further requests. It requires email and password."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully, returns JWT token"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: " + e.getMessage());
        }
    }
}
