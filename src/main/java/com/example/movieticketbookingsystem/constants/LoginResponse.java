package com.example.movieticketbookingsystem.constants;

public class LoginResponse {
    private String token;

    private long expiresIn;

    public LoginResponse(String jwtToken, long expirationTime) {
        this.token = jwtToken;
        this.expiresIn = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}