package com.movieticketbookingsystem.repository;

import com.movieticketbookingsystem.entity.Showtime;
import com.movieticketbookingsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByShowtimeAndSeatNumber(Showtime showtime, int seatNumber);
    List<Ticket> findByShowtimeId(Long showtimeId);
    List<Ticket> findByUserId(Long useIdr);
}
