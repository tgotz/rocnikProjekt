package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean solved = false;

    @ManyToOne
    @JoinColumn(name = "id_character")
    private Character character;  // může být null

    @ManyToOne
    @JoinColumn(name = "id_review")
    private Review review;  // může být null

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "solved_by")
    private User solvedBy;

    @Column(name = "date_reported")
    private LocalDateTime dateReported;

    @Column(name = "date_resolved")
    private LocalDateTime dateResolved;

    // Gettery a settery (nebo použij Lombok @Getter/@Setter/@Data)
    // ...
}
