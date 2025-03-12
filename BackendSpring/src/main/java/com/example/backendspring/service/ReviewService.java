package com.example.backendspring.service;

import com.example.backendspring.model.Review;
import com.example.backendspring.model.Character;
import com.example.backendspring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Vrátí všechny recenze dané postavy
    public List<Review> getReviews(Character character) {
        return reviewRepository.findByCharacter(character);
    }

    // Vloží recenzi do databáze
    public void insertReview(Review review) {
        reviewRepository.save(review);
    }
}
