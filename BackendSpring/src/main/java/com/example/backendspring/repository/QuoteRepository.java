package com.example.backendspring.repository;

import com.example.backendspring.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.backendspring.model.Character;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    // Najde všechny citáty dané postavy
    List<Quote> findByCharacter(Character character);

    // Smaže všechny citáty postavy
    @Transactional
    void deleteByCharacter(Character character);
}
