package com.example.backendspring.controller;

import com.example.backendspring.model.Review;
import com.example.backendspring.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        try {
            reviewService.insertReview(review);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Recenze přidána!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"status\": \"error\", \"message\": \"Chyba při přidání recenze!\"}");
        }
    }
}
