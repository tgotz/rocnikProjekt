package model;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private int reviewId;
    private String reviewText;
    private String authorName;
    private int overallRating;
    private int attractivenessRating;
    private Date dateAdded;
    private int characterId;
    public Review(){

    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public int getAttractivenessRating() {
        return attractivenessRating;
    }

    public void setAttractivenessRating(int attractivenessRating) {
        this.attractivenessRating = attractivenessRating;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
}
