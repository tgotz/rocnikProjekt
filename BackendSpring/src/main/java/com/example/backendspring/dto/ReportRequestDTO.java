package com.example.backendspring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportRequestDTO {
    private String description;

    @JsonProperty("id_character")
    private Integer idCharacter;

    @JsonProperty("id_review")
    private Integer idReview;

    // gettery a settery
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdCharacter() {
        return idCharacter;
    }

    public void setIdCharacter(Integer idCharacter) {
        this.idCharacter = idCharacter;
    }

    public Integer getIdReview() {
        return idReview;
    }

    public void setIdReview(Integer idReview) {
        this.idReview = idReview;
    }
}
