package com.movieticketbookingsystem.controller;

import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Operation(
            summary = "Add a new movie",
            description = "This endpoint adds a new movie to the system with the provided movie details."
    )
    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        try {

            Movie savedMovie = movieService.addMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Update a  movie",
            description = "This endpoint updates the details of an existing movie using the provided movie ID and updated data."
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @Operation(
            summary = "Delete a movie",
            description = "This endpoint deletes the movie identified by the given ID from the system."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all movies",
            description = "This endpoint retrieves a list of all movies"
    )
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @Operation(
            summary = "Get movie by ID",
            description = "This endpoint retrieves a movie based on the provided movie ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

}
