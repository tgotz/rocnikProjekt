package com.example.backendspring.repository;

import com.example.backendspring.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backendspring.model.Character;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {


    List<Review> findByCharacter(Character character);
}
