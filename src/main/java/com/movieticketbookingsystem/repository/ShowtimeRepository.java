package com.movieticketbookingsystem.repository;

import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.entity.Showtime;
import com.movieticketbookingsystem.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovie(Movie movie);
    List<Showtime> findByTheater(Theater theater);
//    List<Showtime> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Query to check for overlapping showtimes for a specific theater
    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater " +
            "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Showtime> findOverlappingShowtimes(Theater theater, LocalDateTime startTime, LocalDateTime endTime);
}