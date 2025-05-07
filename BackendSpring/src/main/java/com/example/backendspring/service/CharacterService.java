package com.example.backendspring.service;

import com.example.backendspring.model.*;
import com.example.backendspring.model.Character;
import com.example.backendspring.repository.*;
import com.example.backendspring.dto.CharacterLeaderboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private ActorService actorService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CharacterMovieService characterMovieService;

    // adding a new character
    public Map<String, String> addCharacter(Character character, List<String> quotes) {
        try {
            int actorId = (character.getActor() != null) ?
                    actorService.getActorId(character.getActor().getName()) : -1;

            int dabberId = (character.getDabber() != null) ?
                    actorService.getActorId(character.getDabber().getName()) : -1;

            if (actorId == -1 && character.getActor() != null) {
                actorId = actorService.insertActor(character.getActor().getName());
            }
            if (dabberId == -1 && character.getDabber() != null) {
                dabberId = actorService.insertActor(character.getDabber().getName());
            }

            character.setActor(actorService.findById(actorId));
            character.setDabber(actorService.findById(dabberId));
            character.setDateAdded(new Date());


            for (Movie movie : character.getMovies()) {
                movie.setId(movieService.getMovieId(movie.getNameMovie()));
                if (movie.getId() == -1) {
                    movie.setId(movieService.insertMovie(movie.getNameMovie()));
                }
            }
            Character savedCharacter = characterRepository.save(character);

            for (String quote : quotes) {
                if (!quote.isEmpty()) {
                    quoteService.insertQuote(quote, savedCharacter);
                }
            }
            return Map.of("status", "success", "message", "Postava byla úspěšně přidána!");
        } catch (Exception e){
            System.out.println(e);
            return Map.of("status", "error", "message", "Nastala chyba při přidávání postavy.");

        }
    }

    public void updateCharacter(Character character, User user, List<String> quotes) {
        Character existingCharacter = characterRepository.findById(character.getId())
                .orElseThrow(() -> new RuntimeException("Postava nenalezena"));


        int actorId = (character.getActor() != null && character.getActor().getName() != null) ?
                actorService.getActorId(character.getActor().getName()) : -1;

        int dabberId = (character.getDabber() != null && character.getDabber().getName() != null) ?
                actorService.getActorId(character.getDabber().getName()) : -1;

        if (actorId == -1 && character.getActor() != null && character.getActor().getName() != null) {
            actorId = actorService.insertActor(character.getActor().getName());
        }
        if (dabberId == -1 && character.getDabber() != null && character.getDabber().getName() != null) {
            dabberId = actorService.insertActor(character.getDabber().getName());
        }

        character.setActor(actorService.findById(actorId));
        character.setDabber(actorService.findById(dabberId));

        characterMovieService.deleteAssignedFilms(character.getId());
        quoteService.deleteQuotes(character);

        character.setActor(actorService.findById(actorId));
        character.setDabber(actorService.findById(dabberId));

        character.setAddedBy(existingCharacter.getAddedBy());

        if (character.getImageBytes() == null || character.getImageBytes().length == 0) {
            character.setImageBytes(existingCharacter.getImageBytes());
        }

        character.setApprovedBy(user);
        character.setApproved(true);
        character.setDateAdded(new Date());

        for (Movie movie : character.getMovies()) {
            int movieId = movieService.getMovieId(movie.getNameMovie());
            if (movieId == -1) {
                movieId = movieService.insertMovie(movie.getNameMovie());
            }
            movie.setId(movieId);
        }

        Character updatedCharacter = characterRepository.save(character);

        for (String quote : quotes) {
            if (!quote.isEmpty()) {
                quoteService.insertQuote(quote, updatedCharacter);
            }
        }
    }


    public Character getCharacterDetail(int id) {
        return characterRepository.findById(id).orElse(null);
    }

    // gets all approved character
    public List<Character> getCharacters() {
        return characterRepository.findByApprovedTrue();
    }

    // gets all unapproved characters
    public List<Map<String, Object>> getUnapprovedCharactersWithQuotes() {
        List<Character> characters = characterRepository.findByApprovedFalse(); // 🔥 Získáme neschválené postavy

        return characters.stream().map(character -> {
            Map<String, Object> charData = new HashMap<>();
            charData.put("id", character.getId());
            charData.put("name", character.getName());
            charData.put("nickname", character.getNickname());
            charData.put("age", character.getAge());
            charData.put("gender", character.getGender());
            charData.put("type", character.getType());
            charData.put("actorName", character.getActor() != null ? character.getActor().getName() : null);
            charData.put("dabberName", character.getDabber() != null ? character.getDabber().getName() : null);
            charData.put("movies", character.getMovies().stream().map(Movie::getNameMovie).collect(Collectors.toList()));
            charData.put("description", character.getDescription());
            charData.put("image", character.getImageBytes());

            List<Quote> quotes = quoteService.getQuotes(character);
            charData.put("quotes", quotes);

            return charData;
        }).collect(Collectors.toList());

}


    public void approveCharacter(int characterId, int userId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        character.setApproved(true);
        character.setApprovedBy(user);
        characterRepository.save(character);
    }


    // Deletes character and its quotes + deletes actor and film if not used
    public void deleteCharacter(int id, User user) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postava nenalezena"));

        // if character is approved - cannot get deleted by users with role 2 or less
        if(character.isApproved()){
            if(user.getRole().getId() <= 2){
                throw new RuntimeException("Nemáte dostatečné oprávnění");
            }
        }


        quoteService.deleteQuotes(character);
        int actorId = 0;
        if(character.getActor() != null) {
            actorId = character.getActor().getId();
        }
        int dabberId = 0;
        if(character.getDabber() != null) {
            dabberId = character.getDabber().getId();
        }
        List<Movie> movies = new ArrayList<>(character.getMovies());


        characterRepository.deleteById(id);

        if(actorId != 0){
            actorService.deleteActorIfNotUsed(actorId);
        }
        if(dabberId != 0){
            actorService.deleteActorIfNotUsed(dabberId);
        }
        for (Movie movie : movies) {
            movieService.deleteMovieIfNotUsed(movie.getId());
        }
    }


    // for leaderboards
    public List<CharacterLeaderboardDTO> getCharactersBySort(int sort) {
        Sort sortOption;

        switch (sort) {
            case 1:
                sortOption = Sort.by(Sort.Direction.DESC, "overall"); // Nejoblíbenější postavy
                break;
            case 2:
                sortOption = Sort.by(Sort.Direction.ASC, "overall"); // Nejnenáviděnější postavy
                break;
            case 3:
                sortOption = Sort.by(Sort.Direction.DESC, "attractiveness"); // Nejatraktivnější postavy
                break;
            default:
                sortOption = Sort.by(Sort.Direction.DESC, "overall"); // Výchozí: Nejoblíbenější
        }

        Pageable pageable = PageRequest.of(0, 50, sortOption);
        return characterRepository.findTopCharacters(pageable).getContent();
    }


    //returns similiar chracters (max 4) - characters from same film
    public List<Character> getSimilarCharacters(int characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Postava s tímto ID nebyla nalezena"));

        List<Movie> characterMovies = character.getMovies();
        if (characterMovies.isEmpty()) {
            return List.of();
        }

        List<Character> charactersInSameMovies = characterRepository.findCharactersByMovies(characterMovies, characterId);

        Random random = new Random();
        int limit = Math.min(4, charactersInSameMovies.size());
        List<Character> similarCharacters = random.ints(0, charactersInSameMovies.size())
                .distinct()
                .limit(limit)
                .mapToObj(charactersInSameMovies::get)
                .toList();

        return similarCharacters;
    }


}
