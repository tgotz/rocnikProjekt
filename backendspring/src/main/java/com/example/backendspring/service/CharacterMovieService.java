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


    public CharacterMovieService(MovieRepository movieRepository,
                                 CharacterRepository characterRepository,
                                 CharacterMovieRepository characterMovieRepository) {
        this.movieRepository = movieRepository;
        this.characterRepository = characterRepository;
        this.characterMovieRepository = characterMovieRepository;
    }


    public void assignFilm(int movieId, int characterId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film nenalezen"));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Postava nenalezena"));

        CharacterMovie characterMovie = new CharacterMovie(character, movie);

        characterMovieRepository.save(characterMovie);
    }

    // Deletes  all films that were assigned to given character
    public void deleteAssignedFilms(int idCharacter) {
        characterMovieRepository.deleteByCharacterId(idCharacter);
    }
}
