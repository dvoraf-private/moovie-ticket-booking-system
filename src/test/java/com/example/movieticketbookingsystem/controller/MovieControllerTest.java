package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.model.Movie;
import com.example.movieticketbookingsystem.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDuration(148);
        movie.setRating(8);
        movie.setReleaseYear(2010);
    }

    // Test for adding a movie
    @Test
    void testAddMovie() {
        // Arrange
        when(movieService.addMovie(any(Movie.class))).thenReturn(movie);

        // Act
        ResponseEntity<Movie> response = movieController.addMovie(movie);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(movie.getTitle(), response.getBody().getTitle());
    }

    // Test for updating a movie
    @Test
    void testUpdateMovie() {
        // Arrange
        when(movieService.updateMovie(eq(1L), any(Movie.class))).thenReturn(movie);

        // Act
        ResponseEntity<Movie> response = movieController.updateMovie(1L, movie);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(movie.getTitle(), response.getBody().getTitle());
    }

    // Test for updating a movie when the movie is not found
    @Test
    void testUpdateMovieNotFound() {
        // Arrange
        when(movieService.updateMovie(eq(1L), any(Movie.class))).thenThrow(new RuntimeException("Movie not found"));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieController.updateMovie(1L, movie);
        });

        // Assert
        assertEquals("Movie not found", exception.getMessage());
    }

    // Test for deleting a movie
    @Test
    void testDeleteMovie() {
        // Arrange
        doNothing().when(movieService).deleteMovie(1L);

        // Act
        ResponseEntity<Void> response = movieController.deleteMovie(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Test for getting a movie by ID
    @Test
    void testGetMovieById() {
        // Arrange
        when(movieService.getMovieById(1L)).thenReturn(movie);

        // Act
        ResponseEntity<Movie> response = movieController.getMovieById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(movie.getId(), response.getBody().getId());
    }

    // Test for getting all movies
    @Test
    void testGetAllMovies() {
        // Arrange
        when(movieService.getAllMovies()).thenReturn(Arrays.asList(movie));

        // Act
        ResponseEntity<List<Movie>> response = movieController.getAllMovies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    // Test for movie not found when trying to get a movie by ID
    @Test
    void testGetMovieByIdNotFound() {
        // Arrange
        when(movieService.getMovieById(1L)).thenThrow(new RuntimeException("Movie not found"));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieController.getMovieById(1L);
        });

        // Assert
        assertEquals("Movie not found", exception.getMessage());
    }
}
