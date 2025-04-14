package com.example.backendspring.repository;

import com.example.backendspring.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Integer> {

    // Najde herce podle jména
    Optional<Actor> findByName(String name);

    List<Actor> findByNameContainingIgnoreCase(String name);

}
