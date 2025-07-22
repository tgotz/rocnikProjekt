package com.example.backendspring.repository;

import com.example.backendspring.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.backendspring.model.Character;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    List<Quote> findByCharacter(Character character);

    //deletes all quotes of a character
    @Transactional
    void deleteByCharacter(Character character);
}
