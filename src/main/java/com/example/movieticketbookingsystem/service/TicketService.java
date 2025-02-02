package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.constants.AppConstants;
import com.example.movieticketbookingsystem.model.Movie;
import com.example.movieticketbookingsystem.model.Showtime;
import com.example.movieticketbookingsystem.model.Ticket;
import com.example.movieticketbookingsystem.model.User;
import com.example.movieticketbookingsystem.repository.ShowtimeRepository;
import com.example.movieticketbookingsystem.repository.TicketRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket bookTicket(Long userId, Long showtimeId, int seatNumber) {
        // Get the max seats per showtime from the constant
        int maxSeatsPerShowtime = AppConstants.MAX_SEATS_PER_SHOWTIME;

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow(() -> new ResourceNotFoundException("Showtime not found"));

        // Check if the seat is already booked
        if (!ticketRepository.findByShowtimeAndSeatNumber(showtime, seatNumber).isEmpty()) {
            throw new RuntimeException("Seat already booked for this showtime");
        }
        // Check if the number of booked seats exceeds the limit
        if (ticketRepository.findByShowtimeId(showtimeId).size() >= maxSeatsPerShowtime) {

            throw new RuntimeException("No seats available for this showtime");
        }

        double price = calculateTicketPrice(showtime.getMovie());
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShowtime(showtime);
        ticket.setSeatNumber(seatNumber);
        ticket.setPrice(price);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getBookingsForUser(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    // Fetch a Ticket by ID
    public Ticket getTicketId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found!"));
    }

    public double calculateTicketPrice(Movie movie) {

        double price = AppConstants.BASE_MOVIE_PRICE;
        // Calculate price adjustment based on movie duration
        int duration = movie.getDuration();
        if (duration > 120) {
            price += 30;
        } else if (duration > 90) {
            price += 20;
        }

        // Calculate price adjustment based on movie rating
        double rating = movie.getRating();
        if (rating >= 9.0) {
            price += 30; // Premium price for highly rated movies
        } else if (rating>= 8.0) {
            price += 20;
        } else if (rating >= 7.0) {
            price += 10;
        }

        // Calculate price adjustment based on release year
        int currentYear = LocalDate.now().getYear();
        int releaseYear = movie.getReleaseYear();
        if (releaseYear == currentYear) {
            price += 20;
        } else if (releaseYear >= currentYear - 3) {
            price += 10;
        } else if (releaseYear < currentYear - 5) {
            price -= 20;
        }

        return price;
    }
}