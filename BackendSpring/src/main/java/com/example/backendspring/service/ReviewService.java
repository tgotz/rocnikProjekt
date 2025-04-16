package com.example.backendspring.service;

import com.example.backendspring.model.Review;
import com.example.backendspring.model.Character;
import com.example.backendspring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviews(Character character) {
        return reviewRepository.findByCharacter(character);
    }

    public void insertReview(Review review) {
        reviewRepository.save(review);
    }

    //deletes review + checks if user is author of the review or is role 3+
    public void deleteReview(int reviewId, int currentUserId, int currentUserRole) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Recenze nenalezena"));

        boolean isAdminOrMod = currentUserRole >= 3;
        boolean isOwner = review.getUser().getId() == currentUserId;
        System.out.println(isOwner);
        System.out.println(isAdminOrMod);
        if (!isAdminOrMod && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nemáš oprávnění smazat tuto recenzi");
        }
        reviewRepository.deleteById(reviewId);
    }

}
