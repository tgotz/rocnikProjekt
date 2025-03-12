package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "characters_movies")
public class CharacterMovie {

    @EmbeddedId
    private CharacterMovieId id;

    @ManyToOne
    @MapsId("characterId")
    @JoinColumn(name = "id_character")  // Opraveno!
    private Character character;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "id_movie")  // Opraveno!
    private Movie movie;

    // ✅ Konstruktor
    public CharacterMovie() {}

    public CharacterMovie(Character character, Movie movie) {
        this.character = character;
        this.movie = movie;
        this.id = new CharacterMovieId(character.getId(), movie.getId());  // Oprava
    }

    // ✅ Gettery
    public Character getCharacter() {
        return character;
    }

    public Movie getMovie() {
        return movie;
    }

    public CharacterMovieId getId() {
        return id;
    }
}
