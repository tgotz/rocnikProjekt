package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quotes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "character")
@ToString(exclude = "character")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "text_quote", nullable = false, length = 500)
    private String textQuote;

    @ManyToOne
    @JoinColumn(name = "id_character", referencedColumnName = "id")
    private Character character;
}
