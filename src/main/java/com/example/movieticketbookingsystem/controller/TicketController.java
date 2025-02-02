package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.model.Movie;
import com.example.movieticketbookingsystem.model.Ticket;
import com.example.movieticketbookingsystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Operation(
            summary = "Book a ticket",
            description = "This endpoint allows a user to book a ticket for a specific showtime and seat number. The userId, showtimeId, and seatNumber are required as query parameters."
    )
    @PostMapping("/book/")
    public ResponseEntity<Ticket> bookTicket(
            @Parameter(description = "ID of the user booking the ticket") @RequestParam Long userId,
            @Parameter(description = "ID of the showtime for the ticket") @RequestParam Long showtimeId,
            @Parameter(description = "Seat number for the ticket") @RequestParam int seatNumber
    ){
        try {
            Ticket ticket = ticketService.bookTicket(userId, showtimeId, seatNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Movie or Showtime not found
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);  // Bad request for booking failure
        }
    }

    @Operation(
            summary = "Get all tickets booked by a specific user",
            description = "This endpoint retrieves all tickets booked by a specific user. The userId is required as a path parameter."
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(
            @Parameter(description = "ID of the user to retrieve booked tickets") @PathVariable Long userId
    ) {
        try {
            List<Ticket> tickets = ticketService.getBookingsForUser(userId);
            return ResponseEntity.ok(tickets);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // User not found
        }
    }

    @Operation(
            summary = "Get ticket by ID",
            description = "This endpoint retrieves a ticket by its ID. The ticket ID is required as a path parameter."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketId(id);
            return ResponseEntity.ok(ticket);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Ticket not found
        }
    }
}
