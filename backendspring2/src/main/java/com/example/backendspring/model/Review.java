package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"character", "user"})
@ToString(exclude = {"character", "user"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "overall_rating", nullable = false)
    private int overallRating;

    @Column(name = "attractiveness_rating", nullable = false)
    private int attractivenessRating;

    @CreationTimestamp
    @Column(name = "date_added", nullable = false, updatable = false)
    private Date dateAdded;

    @Column(name = "text_review", nullable = false, length = 2000)
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "id_character", nullable = false)
    private Character character;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
