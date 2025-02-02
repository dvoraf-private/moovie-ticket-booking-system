package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.exception.InvalidShowtimeDurationException;
import com.example.movieticketbookingsystem.exception.InvalidShowtimeException;
import com.example.movieticketbookingsystem.exception.ResourceNotFoundException;
import com.example.movieticketbookingsystem.model.Movie;
import com.example.movieticketbookingsystem.model.Showtime;
import com.example.movieticketbookingsystem.model.Theater;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.TheaterRepository;
import com.example.movieticketbookingsystem.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;


    @Autowired
    private TheaterRepository theaterRepository;


    public Showtime addShowtime(Long movieId, Long theaterId, Showtime showtimeDetails) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new ResourceNotFoundException("Theater not found"));
        showtimeDetails.setMovie(movie);
        showtimeDetails.setTheater(theater);
        System.out.println("im here 11!!");

        // run validations
        runShowtimeValidationBeforeSave(showtimeDetails, movie);
        // Save the new showtime
        return showtimeRepository.save(showtimeDetails);
    }

    public Showtime updateShowtime(Long showtimeId, Showtime showtimeDetails) {
        // Find the Showtime entity by ID or throw a custom exception if not found
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found for this id: " + showtimeId));

        runShowtimeValidationBeforeSave(showtimeDetails, showtime.getMovie());

        // Update the showtime fields with the new details
        showtime.setStartTime(showtimeDetails.getStartTime());
        showtime.setEndTime(showtimeDetails.getEndTime());


        // Save the updated showtime and return it (no casting needed)
        return showtimeRepository.save(showtime);  // Returning Showtime directly
    }


    public void deleteShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow(() -> new ResourceNotFoundException("Showtime not found"));
        showtimeRepository.delete(showtime);
    }

    public List getAllShowtimesForMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        return showtimeRepository.findByMovie(movie);
    }

    public List getAllShowtimesForTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new ResourceNotFoundException("Theater not found"));
        return showtimeRepository.findByTheater(theater);
    }

    // Method to validate if the showtime overlaps with existing showtimes for the same theater
    private void validateShowtimeOverlap(Showtime showtime) {
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(
                showtime.getTheater(), showtime.getStartTime(), showtime.getEndTime());

        if (!overlappingShowtimes.isEmpty()) {
            System.out.println("im here 33!!");
            throw new RuntimeException("There is already an overlapping showtime in this theater.");
        }
    }

    private void validateShowtimeDuration(Showtime showtimeDetails, Movie movie) {
        // Calculate the duration between startTime and endTime
        long durationInMinutes = java.time.Duration.between(showtimeDetails.getStartTime(), showtimeDetails.getEndTime()).toMinutes();

        // Compare the duration of the movie and the showtime duration
        if (durationInMinutes < movie.getDuration()) {
            throw new InvalidShowtimeDurationException("The showtime duration must be at least " + movie.getDuration() + " minutes for this movie.");
        }
    }

    private void validateStartTimeBeforeEndTime(Showtime showtime) {
        if (showtime.getStartTime().isAfter(showtime.getEndTime())) {
            throw new InvalidShowtimeException("The start time must be before the end time.");
        }
    }

    private void runShowtimeValidationBeforeSave(Showtime showtimeDetails, Movie movie) {
        validateStartTimeBeforeEndTime(showtimeDetails);
        validateShowtimeOverlap(showtimeDetails);
        validateShowtimeDuration(showtimeDetails, movie);
    }
}
