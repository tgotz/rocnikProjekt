package com.example.backendspring.repository;

import com.example.backendspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Najde uživatele podle jména
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
