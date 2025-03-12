package com.example.backendspring.repository;  // ✅ Oprava package

import com.example.backendspring.model.CharacterMovie;
import com.example.backendspring.model.CharacterMovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CharacterMovieRepository extends JpaRepository<CharacterMovie, CharacterMovieId> {

        // ✅ Opravené smazání všech filmů spojených s postavou
        @Transactional
        @Modifying
        @Query("DELETE FROM CharacterMovie cm WHERE cm.id.characterId = :characterId")
        void deleteByCharacterId(@Param("characterId") int characterId);

        // ✅ Opravené smazání všech postav spojených s filmem
        @Transactional
        @Modifying
        @Query("DELETE FROM CharacterMovie cm WHERE cm.id.movieId = :movieId")
        void deleteByMovieId(@Param("movieId") int movieId);
    }
