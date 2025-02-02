package com.example.movieticketbookingsystem.exception;

public class InvalidShowtimeException extends RuntimeException {
    public InvalidShowtimeException(String message) {
        super(message);
    }
}