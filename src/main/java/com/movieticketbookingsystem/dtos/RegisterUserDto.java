package com.movieticketbookingsystem.dtos;

import com.movieticketbookingsystem.constants.AppConstants;

public class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;

    private AppConstants.Role role;

    public RegisterUserDto(String email, String password, String fullName, AppConstants.Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public AppConstants.Role getRole() {
        return role;
    }
}