package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.model.Showtime;
import com.example.movieticketbookingsystem.model.Ticket;
import com.example.movieticketbookingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByShowtimeAndSeatNumber(Showtime showtime, int seatNumber);
    List<Ticket> findByShowtimeId(Long showtimeId);
    List<Ticket> findByUserId(Long useIdr);
}
