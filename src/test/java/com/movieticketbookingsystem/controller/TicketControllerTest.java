package com.movieticketbookingsystem.controller;

import com.movieticketbookingsystem.entity.Ticket;
import com.movieticketbookingsystem.service.TicketService;
import com.movieticketbookingsystem.exception.ResourceNotFoundException;  // Custom exception
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for 'bookTicket' when the ticket is booked successfully
    @Test
    void testBookTicket_Success() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketService.bookTicket(1L, 1L, 1)).thenReturn(ticket);

        // Act
        ResponseEntity<Ticket> response = ticketController.bookTicket(1L, 1L, 1);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    // Test for 'bookTicket' when the ticket booking fails (RuntimeException)
    @Test
    void testBookTicket_BadRequest() {
        // Arrange
        when(ticketService.bookTicket(1L, 1L, 1)).thenThrow(new RuntimeException("Seat already booked"));

        // Act
        ResponseEntity<Ticket> response = ticketController.bookTicket(1L, 1L, 1);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test for 'bookTicket' when movie or showtime is not found (ResourceNotFoundException)
    @Test
    void testBookTicket_NotFound() {
        // Arrange
        when(ticketService.bookTicket(1L, 1L, 1)).thenThrow(new ResourceNotFoundException("Movie or Showtime not found"));

        // Act
        ResponseEntity<Ticket> response = ticketController.bookTicket(1L, 1L, 1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test for 'getTicketsByUser' when tickets are available for the user
    @Test
    void testGetTicketsByUser_Success() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        List<Ticket> tickets = List.of(ticket);
        when(ticketService.getBookingsForUser(1L)).thenReturn(tickets);

        // Act
        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    // Test for 'getTicketsByUser' when user is not found
    @Test
    void testGetTicketsByUser_NotFound() {
        // Arrange
        when(ticketService.getBookingsForUser(1L)).thenThrow(new ResourceNotFoundException("User not found"));

        // Act
        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByUser(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test for 'getTicketById' when the ticket is found
    @Test
    void testGetTicketById_Success() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketService.getTicketId(1L)).thenReturn(ticket);

        // Act
        ResponseEntity<Ticket> response = ticketController.getTicketById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    // Test for 'getTicketById' when ticket is not found
    @Test
    void testGetTicketById_NotFound() {
        // Arrange
        when(ticketService.getTicketId(1L)).thenThrow(new ResourceNotFoundException("Ticket not found"));

        // Act
        ResponseEntity<Ticket> response = ticketController.getTicketById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
