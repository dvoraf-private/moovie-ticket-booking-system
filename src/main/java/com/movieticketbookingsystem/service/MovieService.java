package com.movieticketbookingsystem.service;

import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Add a new movie
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Update an existing movie
    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found!"));

        // TODO refactor to use mapper model
        if (updatedMovie.getTitle() != null) {
            existingMovie.setTitle(updatedMovie.getTitle());
        }
        if (updatedMovie.getGenre() != null) {
            existingMovie.setGenre(updatedMovie.getGenre());
        }
        if (updatedMovie.getRating() != -1) {
            existingMovie.setRating(updatedMovie.getRating());
        }
        if (updatedMovie.getDuration() != -1) {
            existingMovie.setDuration(updatedMovie.getDuration());
        }
        if (updatedMovie.getReleaseYear() != -1) {
            existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        }
        return movieRepository.save(existingMovie);
    }

    // Delete a movie
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    // Fetch all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Fetch a movie by ID
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found!"));
    }
}
