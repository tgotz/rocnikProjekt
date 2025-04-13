package com.example.backendspring.service;

import com.example.backendspring.model.Report;
import com.example.backendspring.model.User;
import com.example.backendspring.model.Character;
import com.example.backendspring.model.Review;

import com.example.backendspring.repository.CharacterRepository;
import com.example.backendspring.repository.ReportRepository;
import com.example.backendspring.repository.ReviewRepository;
import com.example.backendspring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backendspring.dto.ReportRequestDTO;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    ReviewRepository reviewRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report createReport(ReportRequestDTO dto, int reportedById) {
        User user = userRepository.findById(reportedById)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Report report = new Report();
        report.setDescription(dto.getDescription());
        report.setReportedBy(user);
        report.setDateReported(LocalDateTime.now());

        System.out.println(dto);
        System.out.println(dto.getIdCharacter());
        if (dto.getIdCharacter() != null) {
            Character character = characterRepository.findById(dto.getIdCharacter())
                    .orElseThrow(() -> new IllegalArgumentException("Character not found"));
            report.setCharacter(character);
        }

        if (dto.getIdReview() != null) {
            Review review = reviewRepository.findById(dto.getIdReview())
                    .orElseThrow(() -> new IllegalArgumentException("Review not found"));
            report.setReview(review);
        }

        return reportRepository.save(report);
    }

    @Transactional
    public void resolveReport(int reportId, int solvedById) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        User user = userRepository.findById(solvedById)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        report.setSolved(true);
        report.setSolvedBy(user);
        report.setDateResolved(LocalDateTime.now());
    }
    public List<Object[]> getGroupedCharacterReports() {
        return reportRepository.findGroupedCharacterReports();
    }

    public List<Object[]> getGroupedReviewReports() {
        return reportRepository.findGroupedReviewReports();
    }

    public List<Report> getReportsForCharacter(int characterId) {
        return reportRepository.findAllByCharacterIdAndSolvedFalse(characterId);
    }

    public List<Report> getReportsForReview(int reviewId) {
        return reportRepository.findAllByReviewIdAndSolvedFalse(reviewId);
    }

    public void resolveAllForCharacter(int characterId, int resolverId) {
        List<Report> reports = reportRepository.findAllByCharacterIdAndSolvedFalse(characterId);
        resolveReports(reports, resolverId);
    }

    public void resolveAllForReview(int reviewId, int resolverId) {
        List<Report> reports = reportRepository.findAllByReviewIdAndSolvedFalse(reviewId);
        resolveReports(reports, resolverId);
    }
    private void resolveReports(List<Report> reports, int resolverId) {
        User resolver = userRepository.findById(resolverId).orElseThrow();
        for (Report r : reports) {
            r.setSolved(true);
            r.setSolvedBy(resolver);
            r.setDateResolved(LocalDateTime.now());
        }
        reportRepository.saveAll(reports);
    }

}
