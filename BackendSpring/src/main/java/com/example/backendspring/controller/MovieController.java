
package com.example.backendspring.controller;

import com.example.backendspring.model.Movie;
import com.example.backendspring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/search")
    public List<Movie> searchMovies(@RequestParam String query) {
        return movieRepository.findByNameMovieContainingIgnoreCase(query);
    }
}
