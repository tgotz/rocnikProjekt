package com.example.backendspring.controller;

import com.example.backendspring.config.JwtTokenProvider;
import com.example.backendspring.model.Review;
import com.example.backendspring.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    public ReviewController(ReviewService reviewService, JwtTokenProvider jwtTokenProvider) {
        this.reviewService = reviewService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Add a reivew", description = "Adds a reivew. Can be used by all logged in users.")
    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        try {
            reviewService.insertReview(review);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Recenze přidána!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"status\": \"error\", \"message\": \"Chyba při přidání recenze!\"}");
        }
    }
    @Operation(summary = "Delete review.", description = "Deletes review. Can be used by review author or users with role 3+")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable int id, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromCookies(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        int role = jwtTokenProvider.getRoleFromToken(token);
        reviewService.deleteReview(id, userId, role);
        return ResponseEntity.ok("Recenze úspěšně smazána.");
    }


}
