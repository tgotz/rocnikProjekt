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

    // Hledání filmů podle názvu
    public List<Movie> findMovieNames(String input) {
        return movieRepository.findByNameMovieContaining(input);
    }

    // Vrátí ID filmu podle názvu
    public int getMovieIdByName(String filmName) {
        Optional<Movie> film = movieRepository.findByNameMovie(filmName);
        return film.map(Movie::getId).orElse(-1);
    }

    // Vloží film, pokud neexistuje, a vrátí jeho ID
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

    // Smaže film, pokud není vázán na žádnou postavu
    public void deleteMovieIfNotUsed(int filmId) {
        movieRepository.deleteIfNotUsed(filmId);
    }
    public int getMovieId(String movieName) {
        return movieRepository.findByNameMovie(movieName)
                .map(Movie::getId)
                .orElse(-1);
    }
}
