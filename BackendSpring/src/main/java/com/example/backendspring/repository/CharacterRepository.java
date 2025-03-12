package com.example.backendspring.repository;

import com.example.backendspring.model.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.backendspring.dto.CharacterLeaderboardDTO;
import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    // Najde postavu podle ID
    Optional<Character> findById(int id);

    // Najde všechny schválené postavy
    List<Character> findByApprovedTrue();

    // Najde všechny neschválené postavy
    List<Character> findByApprovedFalse();

    // Smaže postavu podle ID
    @Transactional
    void deleteById(int id);

    @Query(value = """
        SELECT c.id AS id, c.name AS name, c.nickname AS nickname, 
               d.name AS dabberName, a.name AS actorName, 
               GROUP_CONCAT(m.name_movie SEPARATOR ', ') AS movies,
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



}
