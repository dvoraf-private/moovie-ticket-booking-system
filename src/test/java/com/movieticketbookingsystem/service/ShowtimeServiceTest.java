package com.movieticketbookingsystem.service;

import com.movieticketbookingsystem.exception.InvalidShowtimeDurationException;
import com.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.movieticketbookingsystem.exception.InvalidShowtimeException;
import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.entity.Showtime;
import com.movieticketbookingsystem.entity.Theater;
import com.movieticketbookingsystem.repository.MovieRepository;
import com.movieticketbookingsystem.repository.ShowtimeRepository;
import com.movieticketbookingsystem.repository.TheaterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Movie movie;
    private Theater theater;
    private Showtime showtime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up test data
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDuration(120);  // movie duration is 120 minutes

        theater = new Theater();
        theater.setId(1L);
        theater.setName("Test Theater");

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setStartTime(LocalDateTime.of(2023, 5, 5, 15, 0));
        showtime.setEndTime(LocalDateTime.of(2023, 5, 5, 17, 0));
    }
    
    @Test
    void testAddShowtime() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        when(showtimeRepository.findOverlappingShowtimes(any(), any(), any())).thenReturn(java.util.Collections.emptyList());
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);

        // Act
        Showtime savedShowtime = showtimeService.addShowtime(1L, 1L, showtime);

        // Assert
        assertNotNull(savedShowtime);
        assertEquals(movie, savedShowtime.getMovie());
        assertEquals(theater, savedShowtime.getTheater());
        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    void testAddShowtimeMovieNotFound() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.addShowtime(1L, 1L, showtime);
        });
        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    void testUpdateShowtime() {
        // Arrange
        Showtime existingShowtime = new Showtime();
        existingShowtime.setId(1L);
        existingShowtime.setStartTime(LocalDateTime.of(2023, 5, 5, 10, 0));
        existingShowtime.setEndTime(LocalDateTime.of(2023, 5, 5, 12, 0));
        existingShowtime.setMovie(movie);

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(existingShowtime));
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(existingShowtime);

        // Act
        Showtime updatedShowtime = showtimeService.updateShowtime(1L, showtime);

        // Assert
        assertNotNull(updatedShowtime);
        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    void testUpdateShowtimeNotFound() {
        // Arrange
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.updateShowtime(1L, showtime);
        });
        assertEquals("Showtime not found for this id: 1", exception.getMessage());
    }

    @Test
    void testDeleteShowtime() {
        // Arrange
        Showtime existingShowtime = new Showtime();
        existingShowtime.setId(1L);

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(existingShowtime));

        // Act
        showtimeService.deleteShowtime(1L);

        // Assert
        verify(showtimeRepository, times(1)).delete(existingShowtime);
    }

    @Test
    void testDeleteShowtimeNotFound() {
        // Arrange
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.deleteShowtime(1L);
        });
        assertEquals("Showtime not found", exception.getMessage());
    }

    @Test
    void testValidateShowtimeDurationTooShort() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        // Arrange
        showtime.setEndTime(showtime.getStartTime().plusMinutes(90)); // less than movie duration (120 minutes)

        // Act & Assert
        InvalidShowtimeDurationException exception = assertThrows(InvalidShowtimeDurationException.class, () -> {
            showtimeService.addShowtime(1L, 1L, showtime);
        });
        assertEquals("The showtime duration must be at least 120 minutes for this movie.", exception.getMessage());
    }

    @Test
    void testStartTimeBeforeEndTimeValidation() {
        // Arrange
        Showtime invalidShowtime = new Showtime();
        invalidShowtime.setStartTime(LocalDateTime.of(2023, 5, 5, 10, 0));  // 10:00 AM
        invalidShowtime.setEndTime(LocalDateTime.of(2023, 5, 5, 9, 0));    // 9:00 AM (before start time)

        // Mock the movie and theater repositories
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDuration(120); // Movie duration is 120 minutes

        Theater theater = new Theater();
        theater.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));

        // Act & Assert
        InvalidShowtimeException exception = assertThrows(InvalidShowtimeException.class, () -> {
            showtimeService.addShowtime(1L, 1L, invalidShowtime);  // This should throw the exception
        });

        // Assert the exception message is correct
        assertEquals("The start time must be before the end time.", exception.getMessage());
    }

    @Test
    void testValidateShowtimeOverlap() {
        // Arrange
        Showtime overlappingShowtime = new Showtime();
        overlappingShowtime.setStartTime(LocalDateTime.of(2023, 5, 5, 11, 0));  // 11:00 AM (overlaps with 10:00 AM to 12:00 PM)
        overlappingShowtime.setEndTime(LocalDateTime.of(2023, 5, 5, 13, 0));    // 1:00 PM

        // Mock the repositories
        when(showtimeRepository.findOverlappingShowtimes(any(Theater.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(overlappingShowtime)); // Simulate that an overlapping showtime is found

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            showtimeService.addShowtime(1L, 1L, showtime);
        });

        // Assert that the exception message is correct
        assertEquals("There is already an overlapping showtime in this theater.", exception.getMessage());

        // Verify that the repository method was called
        verify(showtimeRepository, times(1)).findOverlappingShowtimes(any(Theater.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
