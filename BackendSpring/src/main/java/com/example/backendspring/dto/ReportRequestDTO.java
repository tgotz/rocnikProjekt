package com.example.backendspring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDTO {
    private String description;

    @JsonProperty("id_character")
    private Integer idCharacter;

    @JsonProperty("id_review")
    private Integer idReview;
}
