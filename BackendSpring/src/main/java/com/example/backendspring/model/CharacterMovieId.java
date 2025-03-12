package com.example.backendspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterMovieId implements Serializable {

    @Column(name = "id_character")  // ✅ Opraveno na správný název sloupce v DB
    private int characterId;

    @Column(name = "id_movie")  // ✅ Opraveno na správný název sloupce v DB
    private int movieId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterMovieId that = (CharacterMovieId) o;
        return characterId == that.characterId && movieId == that.movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, movieId);
    }
}
