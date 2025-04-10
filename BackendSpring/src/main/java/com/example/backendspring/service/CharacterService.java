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

    // 📌 Přidání nové postavy (bez mazání filmů/citátů)
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

    // 📌 Aktualizace postavy (bez změny obrázku a addedBy)
    public void updateCharacter(Character character, User user, List<String> quotes) {
        // ✅ Získáme původní postavu z databáze, aby byl zachován `picture` a `addedBy`
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

        // ❌ Smazání filmů a hlášek (ponecháme pokud nemá být změněno)
        characterMovieService.deleteAssignedFilms(character.getId());
        quoteService.deleteQuotes(character);

        // ✅ Nastavení správných hodnot (ponechání obrázku a addedBy)
        character.setActor(actorService.findById(actorId));
        character.setDabber(actorService.findById(dabberId));

        // ✅ Zachování `addedBy` z existující postavy
        character.setAddedBy(existingCharacter.getAddedBy());

        // ✅ Zachování `picture`, pokud nebyl poslán nový
        if (character.getImageBytes() == null || character.getImageBytes().length == 0) {
            character.setImageBytes(existingCharacter.getImageBytes());
        }

        // ✅ Aktualizace schválení
        character.setApprovedBy(user);
        character.setApproved(true);
        character.setDateAdded(new Date());

        // ✅ Aktualizace filmů (přiřazení existujících nebo vytvoření nových)
        for (Movie movie : character.getMovies()) {
            int movieId = movieService.getMovieId(movie.getNameMovie());
            if (movieId == -1) {
                movieId = movieService.insertMovie(movie.getNameMovie());
            }
            movie.setId(movieId);
        }

        // ✅ Uložení aktualizované postavy
        Character updatedCharacter = characterRepository.save(character);

        // ✅ Přidání nových hlášek
        for (String quote : quotes) {
            if (!quote.isEmpty()) {
                quoteService.insertQuote(quote, updatedCharacter);
            }
        }
    }


    // 📌 Vrátí detail postavy
    public Character getCharacterDetail(int id) {
        return characterRepository.findById(id).orElse(null);
    }

    // 📌 Vrátí všechny schválené postavy
    public List<Character> getCharacters() {
        return characterRepository.findByApprovedTrue();
    }

    // 📌 Vrátí všechny neschválené postavy
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

            // 🔥 Načteme quotes z QuoteService
            List<Quote> quotes = quoteService.getQuotes(character);
            charData.put("quotes", quotes);

            return charData;
        }).collect(Collectors.toList());

}

    // 📌 Schválení postavy
    public void approveCharacter(int characterId, int userId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        character.setApproved(true);
        character.setApprovedBy(user);
        characterRepository.save(character);
    }


    // 📌 Smazání postavy a kontrola vazeb na herce a filmy
    public void deleteCharacter(int id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postava nenalezena"));
        System.out.println("zde-1");
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

        System.out.println("zde");

        characterRepository.deleteById(id);

        if(actorId != 0){
            actorService.deleteActorIfNotUsed(actorId);
        }
        if(dabberId != 0){
            actorService.deleteActorIfNotUsed(dabberId);
        }
System.out.println("zde1");
        System.out.println(movies.size());
        for (Movie movie : movies) {
            System.out.println(movie.getId());
            movieService.deleteMovieIfNotUsed(movie.getId());
        }
    }

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

    public List<Character> getSimilarCharacters(int characterId) {
        // Získáme detaily postavy podle ID
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Postava s tímto ID nebyla nalezena"));

        // Získáme seznam filmů, ve kterých je postava
        List<Movie> characterMovies = character.getMovies();
        // Pokud postava není v žádném filmu, vracíme prázdný seznam
        if (characterMovies.isEmpty()) {
            return List.of(); // nebo můžeš vrátit nějakou informaci, že postava nemá žádný film
        }

        // Najdeme všechny postavy, které se vyskytují v těchto filmech, kromě aktuální postavy
        List<Character> charactersInSameMovies = characterRepository.findCharactersByMovies(characterMovies, characterId);
        // Pokud chceme náhodně vybrat 3-4 postavy, použijeme random výběr
        Random random = new Random();
        int limit = Math.min(4, charactersInSameMovies.size()); // Ne více než 4 postavy
        List<Character> similarCharacters = random.ints(0, charactersInSameMovies.size())
                .distinct()
                .limit(limit)
                .mapToObj(charactersInSameMovies::get)
                .toList();

        return similarCharacters;
    }


}
