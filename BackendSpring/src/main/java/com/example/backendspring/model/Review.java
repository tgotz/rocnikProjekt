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
@EqualsAndHashCode(exclude = "character")
@ToString(exclude = "character")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "author_name", nullable = false) // Oprava názvu sloupce
    private String authorName;

    @Column(name = "overall_rating", nullable = false)
    private int overallRating;

    @Column(name = "attractiveness_rating", nullable = false)
    private int attractivenessRating;

    @CreationTimestamp  // Automatické nastavení při vytvoření
    @Column(name = "date_added", nullable = false, updatable = false)
    private Date dateAdded;

    @Column(name = "text_review", nullable = false, length = 2000)
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "id_character", nullable = false) // ✅ Opravený název cizího klíče
    private Character character;

}
