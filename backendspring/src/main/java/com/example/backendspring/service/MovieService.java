package com.example.backendspring.service;

import com.example.backendspring.model.Movie;
import com.example.backendspring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> findMovieNames(String input) {
        return movieRepository.findByNameMovieContaining(input);
    }

    public int getMovieIdByName(String filmName) {
        Optional<Movie> film = movieRepository.findByNameMovie(filmName);
        return film.map(Movie::getId).orElse(-1);
    }

    // inserts film - if it doesn't already exists.  Returns it's id
    public int insertMovie(String filmName) {
        Optional<Movie> existingFilm = movieRepository.findByNameMovie(filmName);
        if (existingFilm.isPresent()) {
            return existingFilm.get().getId();
        }

        Movie newMovie = new Movie();
        newMovie.setNameMovie(filmName);
        movieRepository.save(newMovie);
        return newMovie.getId();
    }

    // deletes film if not used by any characters
    public void deleteMovieIfNotUsed(int filmId) {
        movieRepository.deleteIfNotUsed(filmId);
    }
    public int getMovieId(String movieName) {
        return movieRepository.findByNameMovie(movieName)
                .map(Movie::getId)
                .orElse(-1);
    }
}
