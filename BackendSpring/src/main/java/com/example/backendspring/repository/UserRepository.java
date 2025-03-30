package com.example.backendspring.repository;

import com.example.backendspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Najde uživatele podle jména
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);

    @Query(value = """
    SELECT u.id, u.username, u.email, u.role_id, u.registrationDate, roles.role_name, 
           COUNT(DISTINCT c.id) AS characterCount,
           COUNT(DISTINCT r.id) AS reviewCount
    FROM users u
    LEFT JOIN characters c ON c.added_by = u.id
    LEFT JOIN reviews r ON r.id_user = u.id
    JOIN roles ON roles.id = u.role_id
    WHERE u.id = :userId
    GROUP BY u.id
""", nativeQuery = true)
    List<Object[]> getUserProfile(@Param("userId") int userId);
}
