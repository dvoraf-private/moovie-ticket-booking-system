package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.model.Showtime;
import com.example.movieticketbookingsystem.service.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @Operation(
            summary = "Add a new showtime",
            description = "This endpoint allows the user to add a new showtime for a specific movie and theater. The movieId and theaterId are required as path parameters."
    )
    @PostMapping("/{movieId}/{theaterId}")
    public ResponseEntity<Showtime> addShowtime(@PathVariable Long movieId, @PathVariable Long theaterId, @RequestBody Showtime showtime) {
        try {
            Showtime savedShowtime = showtimeService.addShowtime(movieId, theaterId, showtime);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedShowtime);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if movie or theater not found
        }
    }

    // Update an existing showtime
    @Operation(
            summary = "Update an existing showtime",
            description = "This endpoint allows the user to update an existing showtime. The showtime ID and updated details are required."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Showtime> updateShowtime(@PathVariable Long id, @RequestBody Showtime showtimeDetails) {
        Showtime updatedShowtime = showtimeService.updateShowtime(id, showtimeDetails);
        if (updatedShowtime == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Showtime not found
        }
        return ResponseEntity.ok(updatedShowtime);    }

    // Delete a showtime
    @Operation(
            summary = "Delete a showtime",
            description = "This endpoint allows the user to delete a showtime using its ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Fetch all showtimes for a specific movie",
            description = "This endpoint retrieves all showtimes associated with a specific movie using the movieId."
    )
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Showtime>> getShowtimesForMovie(@PathVariable Long movieId) {
        try {
            List<Showtime> showtimes = showtimeService.getAllShowtimesForMovie(movieId);
            return showtimes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() // No showtimes available for movie
                    : ResponseEntity.ok(showtimes);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Movie not found
        }
    }

    @Operation(
            summary = "Fetch all showtimes for a specific theater",
            description = "This endpoint retrieves all showtimes associated with a specific theater using the theaterId."
    )    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<Showtime>> getShowtimesForTheater(@PathVariable Long theaterId) {
        try {
            List<Showtime> showtimes = showtimeService.getAllShowtimesForTheater(theaterId);
            return showtimes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() // No showtimes available for theater
                    : ResponseEntity.ok(showtimes);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Theater not found
        }
    }
}
