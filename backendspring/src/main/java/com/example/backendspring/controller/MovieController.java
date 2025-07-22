
package com.example.backendspring.controller;

import com.example.backendspring.model.Movie;
import com.example.backendspring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController

@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Operation(summary = "Search movies based on query.", description = "Finds movies that include input query. Used for autofill in forms.")
    @GetMapping("/search")
    public List<Movie> searchMovies(@RequestParam String query) {
        return movieRepository.findByNameMovieContainingIgnoreCase(query);
    }
}
