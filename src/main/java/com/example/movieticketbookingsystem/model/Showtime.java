package com.example.movieticketbookingsystem.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Table(name="showtimes")
@Entity
public class Showtime {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Theater getTheater() {
        return theater;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
