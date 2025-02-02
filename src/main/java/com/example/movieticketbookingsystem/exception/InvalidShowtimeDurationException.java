package com.example.movieticketbookingsystem.exception;

public class InvalidShowtimeDurationException extends RuntimeException {
    public InvalidShowtimeDurationException(String message) {
        super(message);
    }
}