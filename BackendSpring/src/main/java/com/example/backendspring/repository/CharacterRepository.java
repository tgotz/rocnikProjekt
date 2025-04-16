package com.example.backendspring.repository;

import com.example.backendspring.model.Character;
import com.example.backendspring.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.backendspring.dto.CharacterLeaderboardDTO;
import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    Optional<Character> findById(int id);

    List<Character> findByApprovedTrue();

    List<Character> findByApprovedFalse();

    @Transactional
    void deleteById(int id);

    //getting characters for certain leaderboards - gets its average ratings etc
    @Query(value = """
        SELECT c.id AS id, c.name AS name, c.nickname AS nickname, 
               d.name AS dabberName, a.name AS actorName,
                MIN(m.name_movie) AS movies,
                AVG(reviews.overall_rating) AS overall, 
               AVG(reviews.attractiveness_rating) AS attractiveness
        FROM characters c
        LEFT JOIN actors d ON d.id = c.id_dabber
        LEFT JOIN actors a ON a.id = c.id_actor
        JOIN reviews ON reviews.id_character = c.id
        LEFT JOIN characters_movies cm ON c.id = cm.id_character 
        LEFT JOIN movies m ON cm.id_movie = m.id
        GROUP BY c.id
    """, nativeQuery = true)
    Page<CharacterLeaderboardDTO> findTopCharacters(Pageable pageable);

    @Query("SELECT c FROM Character c JOIN c.movies m WHERE m IN :movies AND c.id != :characterId AND c.approved = true")
    List<Character> findCharactersByMovies(
            @Param("movies") List<Movie> movies,
            @Param("characterId") int characterId
    );

    boolean existsByDabberId(int dabberId);
    boolean existsByActorId(int actorId);


}
