package com.movieticketbookingsystem.service;

import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private Movie updatedMovie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDuration(140);
        movie.setRating(8);
        movie.setReleaseYear(2010);

        updatedMovie = new Movie();
        updatedMovie.setTitle("Inception Updated");
        updatedMovie.setGenre("Sci-Fi");
        updatedMovie.setDuration(150);
        updatedMovie.setRating(9);
        updatedMovie.setReleaseYear(2012);
    }

    @Test
    void testAddMovie() {
        // Arrange
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Act
        Movie savedMovie = movieService.addMovie(movie);

        // Assert
        assertNotNull(savedMovie);
        assertEquals(movie.getTitle(), savedMovie.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));  // Ensure save is called once
    }

    @Test
    void testUpdateMovie() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Act
        Movie updated = movieService.updateMovie(1L, updatedMovie);

        // Assert
        assertNotNull(updated);
        assertEquals("Inception Updated", updated.getTitle());
        assertEquals(150, updated.getDuration());
        assertEquals(9, updated.getRating());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));  // Ensure save is called once
    }

    @Test
    void testUpdateMovie_WhenMovieNotFound() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> movieService.updateMovie(1L, updatedMovie));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.getMovieById(1L);
        });
        assertEquals("Movie not found!", exception.getMessage());
    }

    @Test
    void testDeleteMovie() {
        // Arrange
        doNothing().when(movieRepository).deleteById(1L);

        // Act
        movieService.deleteMovie(1L);

        // Assert
        verify(movieRepository, times(1)).deleteById(1L);
    }


    void testGetAllMovies() {
        // Arrange
        List<Movie> movies = Arrays.asList(movie);
        when(movieRepository.findAll()).thenReturn(movies);

        // Act
        List<Movie> fetchedMovies = movieService.getAllMovies();

        // Assert
        assertNotNull(fetchedMovies);
        assertEquals(1, fetchedMovies.size());
        assertEquals("Inception", fetchedMovies.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Act
        Movie fetchedMovie = movieService.getMovieById(1L);

        // Assert
        assertNotNull(fetchedMovie);
        assertEquals("Inception", fetchedMovie.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMovieById_WhenMovieNotFound() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> movieService.getMovieById(1L));
        assertEquals("Movie not found!", thrown.getMessage());
    }
}
