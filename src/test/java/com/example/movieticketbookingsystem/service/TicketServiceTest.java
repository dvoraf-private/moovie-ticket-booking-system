package com.example.movieticketbookingsystem.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.model.Movie;
import com.example.movieticketbookingsystem.model.Showtime;
import com.example.movieticketbookingsystem.model.Ticket;
import com.example.movieticketbookingsystem.model.User;
import com.example.movieticketbookingsystem.repository.ShowtimeRepository;
import com.example.movieticketbookingsystem.repository.TicketRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    private User user;
    private Showtime showtime;
    private Ticket ticket;
    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock data
        user = new User();
        user.setId(111);

        movie = new Movie();
        movie.setId(1L);
        movie.setDuration(120);
        movie.setRating(8);
        movie.setReleaseYear(2022);

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDate.now().atStartOfDay());
        showtime.setEndTime(showtime.getStartTime().plusMinutes(120));

        ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShowtime(showtime);
        ticket.setSeatNumber(1);
        ticket.setPrice(50.0);
    }

    @Test
    void testBookTicketUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.bookTicket(1L, 1L, 1);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testBookTicketShowtimeNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.bookTicket(1L, 1L, 1);
        });

        assertEquals("Showtime not found", exception.getMessage());
    }

    @Test
    void testBookTicketSeatAlreadyBooked() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeAndSeatNumber(showtime, 1)).thenReturn(List.of(ticket));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.bookTicket(1L, 1L, 1);
        });

        assertEquals("Seat already booked for this showtime", exception.getMessage());
    }

    @Test
    void testBookTicketNoSeatsAvailable() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeAndSeatNumber(showtime, 1)).thenReturn(List.of());
        when(ticketRepository.findByShowtimeId(1L)).thenReturn(List.of(new Ticket(), new Ticket())); // Simulate 2 tickets already booked

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.bookTicket(1L, 1L, 1);
        });

        assertEquals("No seats available for this showtime", exception.getMessage());
    }

    @Test
    void testBookTicketSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeAndSeatNumber(showtime, 1)).thenReturn(List.of());
        when(ticketRepository.findByShowtimeId(1L)).thenReturn(List.of()); // Simulate no tickets booked yet
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket); // Simulate no tickets booked yet

        Ticket savedTicket = ticketService.bookTicket(1L, 1L, 1);

        assertNotNull(savedTicket);
        assertEquals(50.0, savedTicket.getPrice());
        assertEquals(user, savedTicket.getUser());
        assertEquals(showtime, savedTicket.getShowtime());
        assertEquals(1, savedTicket.getSeatNumber());
    }

    @Test
    void testGetBookingsForUser() {
        when(ticketRepository.findByUserId(1L)).thenReturn(List.of(ticket));

        List<Ticket> bookings = ticketService.getBookingsForUser(1L);

        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals(ticket, bookings.get(0));
    }

    @Test
    void testGetTicketIdNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.getTicketId(1L);
        });

        assertEquals("Ticket not found!", exception.getMessage());
    }

    @Test
    void testCalculateTicketPrice() {
        double price = ticketService.calculateTicketPrice(movie);

        assertEquals(100.0, price);
    }
}
