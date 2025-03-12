package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "actors")  // Odpovídá tabulce v databázi
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Primární klíč

    @Column(nullable = false, unique = true)
    private String name;  // Jméno herce
}
