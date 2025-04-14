package com.example.backendspring.repository;

import com.example.backendspring.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // 🔹 Najde filmy obsahující určitý text (vyhledávání podle části názvu)
    List<Movie> findByNameMovieContaining(String input);

    // 🔹 Najde film podle přesného názvu
    Optional<Movie> findByNameMovie(String filmName);

    // 🔹 Smaže film, pokud není přiřazen žádné postavě
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM movies WHERE id = :movieId " +
            "AND NOT EXISTS (SELECT 1 FROM characters_movies WHERE id_movie = :movieId)",
            nativeQuery = true)
    void deleteIfNotUsed(int movieId);

    List<Movie> findByNameMovieContainingIgnoreCase(String name);

}
