package com.example.backendspring.service;

import com.example.backendspring.model.Character;
import com.example.backendspring.model.CharacterMovie;
import com.example.backendspring.model.CharacterMovieId;
import com.example.backendspring.model.Movie;
import com.example.backendspring.repository.CharacterMovieRepository;
import com.example.backendspring.repository.CharacterRepository;
import com.example.backendspring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterMovieService {

    private final MovieRepository movieRepository;
    private final CharacterRepository characterRepository;
    private final CharacterMovieRepository characterMovieRepository;

    // ✅ Konstruktorová injekce (NENÍ POTŘEBA @Autowired)
    public CharacterMovieService(MovieRepository movieRepository,
                                 CharacterRepository characterRepository,
                                 CharacterMovieRepository characterMovieRepository) {
        this.movieRepository = movieRepository;
        this.characterRepository = characterRepository;
        this.characterMovieRepository = characterMovieRepository;
    }

    // ✅ Opravené přiřazení postavy k filmu
    public void assignFilm(int movieId, int characterId) {
        // Načtení objektů Movie a Character z databáze
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film nenalezen"));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Postava nenalezena"));

        // Vytvoření vazby
        CharacterMovie characterMovie = new CharacterMovie(character, movie);

        // Uložení do databáze
        characterMovieRepository.save(characterMovie);
    }

    // Smazání všech filmů spojených s postavou
    public void deleteAssignedFilms(int idCharacter) {
        characterMovieRepository.deleteByCharacterId(idCharacter);
    }
}
