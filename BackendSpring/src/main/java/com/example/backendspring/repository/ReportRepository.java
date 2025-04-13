package com.example.backendspring.repository;

import com.example.backendspring.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("SELECT r.character.id, r.character.name, COUNT(r), MAX(r.dateReported) " +
            "FROM Report r WHERE r.character IS NOT NULL AND r.solved = false " +
            "GROUP BY r.character.id, r.character.name ORDER BY COUNT(r) DESC")
    List<Object[]> findGroupedCharacterReports();

    @Query("SELECT r.review.id, r.review.user.username, COUNT(r), MAX(r.dateReported) " +
            "FROM Report r WHERE r.review IS NOT NULL AND r.solved = false " +
            "GROUP BY r.review.id, r.review.user.username ORDER BY COUNT(r) DESC")
    List<Object[]> findGroupedReviewReports();

    List<Report> findAllByCharacterIdAndSolvedFalse(Integer characterId);
    List<Report> findAllByReviewIdAndSolvedFalse(Integer reviewId);


}
