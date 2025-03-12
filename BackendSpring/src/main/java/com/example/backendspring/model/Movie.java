package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Primární klíč

    @Column(name = "name_movie", nullable = false, unique = true)  // ✅ Opravený název sloupce
    private String nameMovie;
}
