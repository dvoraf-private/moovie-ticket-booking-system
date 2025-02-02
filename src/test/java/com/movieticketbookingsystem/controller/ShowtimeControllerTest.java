package com.movieticketbookingsystem.controller;

import com.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.entity.Showtime;
import com.movieticketbookingsystem.entity.Theater;
import com.movieticketbookingsystem.service.ShowtimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowtimeControllerTest {

    @InjectMocks
    private ShowtimeController showtimeController;

    @Mock
    private ShowtimeService showtimeService;

    private Showtime showtime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setStartTime(LocalDateTime.parse("2025-01-31T10:00:00"));
        showtime.setEndTime(LocalDateTime.parse("2025-01-31T12:00:00"));

        showtime.setTheater(new Theater());
        showtime.setMovie(new Movie());
    }

    // Test for adding a showtime
    @Test
    void testAddShowtime() {
        // Arrange
        when(showtimeService.addShowtime(eq(1L), eq(1L), any(Showtime.class))).thenReturn(showtime);

        // Act
        ResponseEntity<Showtime> response = showtimeController.addShowtime(1L, 1L, showtime);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(showtime.getStartTime(), response.getBody().getStartTime());
    }

    // Test for updating a showtime
    @Test
    void testUpdateShowtime() {
        // Arrange
        when(showtimeService.updateShowtime(eq(1L), any(Showtime.class))).thenReturn(showtime);

        // Act
        ResponseEntity<Showtime> response = showtimeController.updateShowtime(1L, showtime);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(showtime.getStartTime(), response.getBody().getStartTime());
    }

    // Test for updating a showtime when it doesn't exist
    @Test
    void testUpdateShowtimeNotFound() {
        // Arrange
        when(showtimeService.updateShowtime(eq(1L), any(Showtime.class))).thenThrow(new RuntimeException("Showtime not found"));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            showtimeController.updateShowtime(1L, showtime);
        });

        // Assert
        assertEquals("Showtime not found", exception.getMessage());
    }

    // Test for deleting a showtime
    @Test
    void testDeleteShowtime() {
        // Arrange
        doNothing().when(showtimeService).deleteShowtime(1L);

        // Act
        ResponseEntity<Void> response = showtimeController.deleteShowtime(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetShowtimesForMovie_Success() {
        // Arrange
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setStartTime(LocalDateTime.parse("2025-01-31T14:00:00"));
        showtime1.setEndTime(LocalDateTime.parse("2025-01-31T16:00:00"));

        List<Showtime> showtimes = List.of(showtime1);
        when(showtimeService.getAllShowtimesForMovie(1L)).thenReturn(showtimes);

        // Act
        ResponseEntity<List<Showtime>> response = showtimeController.getShowtimesForMovie(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    // Test for 'getShowtimesForMovie' when no showtimes are available
    @Test
    void testGetShowtimesForMovie_NoContent() {
        // Arrange
        when(showtimeService.getAllShowtimesForMovie(1L)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Showtime>> response = showtimeController.getShowtimesForMovie(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test for 'getShowtimesForMovie' when movie is not found
    @Test
    void testGetShowtimesForMovie_NotFound() {
        // Arrange
        when(showtimeService.getAllShowtimesForMovie(1L)).thenThrow(new ResourceNotFoundException("Movie not found"));

        // Act
        ResponseEntity<List<Showtime>> response = showtimeController.getShowtimesForMovie(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test for 'getShowtimesForTheater'
    @Test
    void testGetShowtimesForTheater_Success() {
        // Arrange
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setStartTime(LocalDateTime.parse("2025-01-31T14:00:00"));
        showtime1.setEndTime(LocalDateTime.parse("2025-01-31T16:00:00"));

        List<Showtime> showtimes = List.of(showtime1);
        when(showtimeService.getAllShowtimesForTheater(1L)).thenReturn(showtimes);

        // Act
        ResponseEntity<List<Showtime>> response = showtimeController.getShowtimesForTheater(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    // Test for 'getShowtimesForTheater' when no showtimes are available
    @Test
    void testGetShowtimesForTheater_NoContent() {
        // Arrange
        when(showtimeService.getAllShowtimesForTheater(1L)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Showtime>> response = showtimeController.getShowtimesForTheater(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Test for 'getShowtimesForTheater' when theater is not found
    @Test
    void testGetShowtimesForTheater_NotFound() {
        // Arrange
        when(showtimeService.getAllShowtimesForTheater(1L)).thenThrow(new ResourceNotFoundException("Theater not found"));

        // Act
        ResponseEntity<List<Showtime>> response = showtimeController.getShowtimesForTheater(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
