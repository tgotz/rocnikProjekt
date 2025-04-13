package com.example.backendspring.controller;

import com.example.backendspring.config.JwtTokenProvider;
import com.example.backendspring.dto.ReportRequestDTO;
import com.example.backendspring.model.Report;
import com.example.backendspring.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PostMapping("/add")
    public ResponseEntity<Report> createReport(@RequestBody ReportRequestDTO dto, @RequestParam int userId) {
        Report created = reportService.createReport(dto, userId);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<String> resolveReport(@PathVariable int id, @RequestParam int solvedById) {
        reportService.resolveReport(id, solvedById);
        return ResponseEntity.ok("Report vyřešen.");
    }

    @GetMapping("/characters")
    public List<Object[]> getCharacterReports() {
        return reportService.getGroupedCharacterReports();
    }

    @GetMapping("/reviews")
    public List<Object[]> getReviewReports() {
        return reportService.getGroupedReviewReports();
    }

    @GetMapping("/character/{id}")
    public List<Report> getCharacterReports(@PathVariable int id) {
        return reportService.getReportsForCharacter(id);
    }

    @GetMapping("/review/{id}")
    public List<Report> getReviewReports(@PathVariable int id) {
        return reportService.getReportsForReview(id);
    }

    @PostMapping("/resolve/character/{id}")
    public ResponseEntity<?> resolveCharacterReports(@PathVariable int id, HttpServletRequest request) {
        int userId = jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.getTokenFromCookies(request));
        reportService.resolveAllForCharacter(id, userId);
        return ResponseEntity.ok("Vyřešeno.");
    }

    @PostMapping("/resolve/review/{id}")
    public ResponseEntity<?> resolveReviewReports(@PathVariable int id, HttpServletRequest request) {
        int userId = jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.getTokenFromCookies(request));
        reportService.resolveAllForReview(id, userId);
        return ResponseEntity.ok("Vyřešeno.");
    }
}
