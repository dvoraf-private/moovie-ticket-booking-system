package com.example.movieticketbookingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.Id;

@Table(name="movies")
@Entity
public class Movie {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private int duration; // in minutes

    @Min(value = 1, message = "Rating must be between 1 and 10")
    @Max(value = 10, message = "Rating must be between 1 and 10")
    private int rating;

    @Min(value = 1900, message = "Release year must be a valid year")
    @Max(value = 2100, message = "Release year must be a valid year")
    private int releaseYear;

    public Movie() {}
    public Movie(String title, String genre, int duration, int rating, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public int getRating() {
        return rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
