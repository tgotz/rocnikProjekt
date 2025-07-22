package com.example.backendspring.controller;

import com.example.backendspring.config.JwtTokenProvider;
import com.example.backendspring.dto.ReportRequestDTO;
import com.example.backendspring.model.Report;
import com.example.backendspring.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get all reports.", description = "Gets all reports. Can be used by users with role 3+")
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @Operation(summary = "Add report", description = "Adds a new report. Can be used by any logged in user.")
    @PostMapping("/add")
    public ResponseEntity<Report> createReport(@RequestBody ReportRequestDTO dto, @RequestParam int userId) {
        Report created = reportService.createReport(dto, userId);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Resolve a report.", description = "Resolves a report. Can be used by users with role 3+ from report dashboard.")
    @PutMapping("/{id}/resolve")
    public ResponseEntity<String> resolveReport(@PathVariable int id, @RequestParam int solvedById) {
        reportService.resolveReport(id, solvedById);
        return ResponseEntity.ok("Report vyřešen.");
    }

    @Operation(summary = "Gets report of characters.", description = "Gets all characters that have any unresolved reports, gets their report count, last report date etc. Can be used by users with role 3+")
    @GetMapping("/characters")
    public List<Object[]> getCharacterReports() {
        return reportService.getGroupedCharacterReports();
    }

    @Operation(summary = "Gets report of review.", description = "Gets all reviews that have any unresolved reports, gets their report count, last report date etc. Can be used by users with role 3+")
    @GetMapping("/reviews")
    public List<Object[]> getReviewReports() {
        return reportService.getGroupedReviewReports();
    }

    @Operation(summary = "Gets all reports of a specific character", description = "Gets all unresolved reports of a specific character, can be used by user with role 3+")
    @GetMapping("/character/{id}")
    public List<Report> getCharacterReports(@PathVariable int id) {
        return reportService.getReportsForCharacter(id);
    }

    @Operation(summary = "Gets all reports of a specific review", description = "Gets all unresolved reports of a specific review, can be used by user with role 3+")
    @GetMapping("/review/{id}")
    public List<Report> getReviewReports(@PathVariable int id) {
        return reportService.getReportsForReview(id);
    }

    @Operation(summary = "Resolve ALL reports of a character.", description = "Resolves ALL reports of a character., can be used by user with role 3+")
    @PostMapping("/resolve/character/{id}")
    public ResponseEntity<?> resolveCharacterReports(@PathVariable int id, HttpServletRequest request) {
        int userId = jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.getTokenFromCookies(request));
        reportService.resolveAllForCharacter(id, userId);
        return ResponseEntity.ok("Vyřešeno.");
    }

    @Operation(summary = "Resolve ALL reports of a review.", description = "Resolves ALL reports of a review., can be used by user with role 3+")
    @PostMapping("/resolve/review/{id}")
    public ResponseEntity<?> resolveReviewReports(@PathVariable int id, HttpServletRequest request) {
        int userId = jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.getTokenFromCookies(request));
        reportService.resolveAllForReview(id, userId);
        return ResponseEntity.ok("Vyřešeno.");
    }
}
