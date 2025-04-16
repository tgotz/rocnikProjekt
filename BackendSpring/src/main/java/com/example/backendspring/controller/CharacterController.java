package com.example.backendspring.controller;

import com.example.backendspring.config.SecurityConfig;
import com.example.backendspring.model.*;
import com.example.backendspring.model.Character;
import com.example.backendspring.service.CharacterService;
import com.example.backendspring.service.QuoteService;
import com.example.backendspring.service.ReviewService;
import com.example.backendspring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.backendspring.config.JwtTokenProvider;

import java.util.*;

@RestController
@RequestMapping("/api/character")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CharacterController {
    private final JwtTokenProvider jwtTokenProvider;
    private final CharacterService characterService;
    private final ReviewService reviewService;
    private final QuoteService quoteService;
    private final UserService userService;

    public CharacterController(CharacterService characterService, ReviewService reviewService, QuoteService quoteService, JwtTokenProvider jwtTokenProvider, SecurityConfig securityConfig, UserService userService) {
        this.characterService = characterService;
        this.reviewService = reviewService;
        this.quoteService = quoteService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }


    @Operation(summary = "Gets all characters", description = "Gets all approved characters. Is used  for homepage and character dashboard.")
    @GetMapping
    public ResponseEntity<List<Character>> getAllCharacters() {
        List<Character> characters = characterService.getCharacters();
        return ResponseEntity.ok(characters);
    }


    @Operation(summary = "Get character details", description = "Outputs all character details. Is used for character detail page.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCharacterDetail(@PathVariable int id) {
        Map<String, Object> responseData = new HashMap<>();

        try {
            Character character = characterService.getCharacterDetail(id);
            if (character == null) {
                responseData.put("error", "Postava nebyla nalezena.");
                return ResponseEntity.badRequest().body(responseData);
            }

            List<Review> reviewList = reviewService.getReviews(character);
            List<Quote> quotesList = quoteService.getQuotes(character);

            responseData.put("character", character);
            responseData.put("reviews", reviewList);
            responseData.put("quotes", quotesList);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.put("error", "Došlo k chybě při zpracování požadavku.");
            return ResponseEntity.internalServerError().body(responseData);
        }
    }

    @Operation(summary = "Add new character", description = "Adds new characters. If user is role 2+ auto approves the character, otherwise character needs  to get approved to appear on website.")
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> addCharacter(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("gender") String gender,
            @RequestParam("desc") String desc,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "actor", required = false) String actorName,
            @RequestParam(value = "dabber", required = false) String dabberName,
            @RequestParam("movies") String movies,
            @RequestParam(value = "quotes", required = false) String quotes,
            @RequestParam("picture") MultipartFile picture,
            HttpServletRequest request
    ) {
        try {
            // getting List from string - every new line is a new movie
            List<String> movieNames = Arrays.stream(movies.split("\\r?\\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            // same for quotes
            List<String> quoteList = (quotes != null && !quotes.isEmpty())
                    ? Arrays.stream(quotes.split("\\r?\\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList()
                    : List.of();

            // making new character
            Character character = new Character();
            character.setName(name);
            character.setType(type);
            character.setGender(gender);
            character.setDescription(desc);
            character.setAge(age);
            character.setNickname(nickname);
            character.setImageBytes(picture.getBytes());
            Actor actor = new Actor();
            actor.setName(actorName);
            Actor dabber = new Actor();
            dabber.setName(dabberName);
            if(actorName != null && !actorName.isEmpty()) {
                character.setActor(actor);
            }
            if(dabberName != null && !dabberName.isEmpty()) {
                character.setDabber(dabber);
            }
            List<Movie> moviesList = new ArrayList<>();
            for(int i = 0; i < movieNames.size(); i++){
                Movie movie = new Movie();
                movie.setNameMovie(movieNames.get(i));  ;
                moviesList.add(movie);
            }
            character.setMovies(moviesList);
            // adding user who created the character
            String token = jwtTokenProvider.getTokenFromCookies(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                int userId = jwtTokenProvider.getUserIdFromToken(token);
                User user = userService.getUserById(userId);
                character.setAddedBy(user);
                // if user is lvl 2 or more - auto verify the character
                if(user.getRole().getId() >= 2){
                    character.setApproved(true);
                    character.setApprovedBy(user);
                }
            }



            Map<String, String> response = characterService.addCharacter(character, quoteList);




            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Chyba při zpracování obrázku."));
        }
    }


    @Operation(summary = "Approving character", description = "Approving character. Can only be done my users with role 2+")
    @GetMapping("/approve")
    @PreAuthorize("hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<List<Map<String, Object>>> getUnapprovedCharacters() {
        List<Map<String, Object>> characters = characterService.getUnapprovedCharactersWithQuotes();
        return ResponseEntity.ok(characters);
    }

    @Operation(summary = "Deleting character", description = "Deleting character. Can only be done by people with role 2+")
    @DeleteMapping("/delete-character/{id}" )
    @PreAuthorize("hasAnyAuthority('ROLE_2', 'ROLE_3', 'ROLE_4')")
    public ResponseEntity<?> deleteCharacter(@PathVariable int id, HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.getTokenFromCookies(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                int userId = jwtTokenProvider.getUserIdFromToken(token);
                User user = userService.getUserById(userId);
                characterService.deleteCharacter(id, user);
                return ResponseEntity.ok().body("{\"message\": \"Character deleted successfully\"}");
            }else{
                return ResponseEntity.status(500).body("{\"error\": \"Failed to delete character due to insufficient role\"}");
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body("{\"error\": \"Failed to delete character.0\"}");
        }
    }

    @Operation(summary = "Approve character", description = "Approving a character. Can be used by users with role 2+")
    @PostMapping("/approve-character")
    @PreAuthorize("hasAnyAuthority('ROLE_2', 'ROLE_3', 'ROLE_4')") // ✅ Přístup pouze pro moderátory+
    public ResponseEntity<?> approveCharacter(@RequestBody Map<String, Object> requestBody) {

        try {
            int characterId = (int) requestBody.get("id");
            int userId = (int) requestBody.get("userId");

            System.out.println("Character ID: " + characterId);
            System.out.println("User ID: " + userId);

            characterService.approveCharacter(characterId, userId);

            return ResponseEntity.ok().body("{\"message\":\"Character approved successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("{\"error\":\"An error occurred while approving the character.\"}");
        }
    }
    @Operation(summary = "Updating a character", description = "Updates a characters, its movies, quotes, actors etc.")
    @PostMapping("/update-character")
    @PreAuthorize("hasAnyAuthority('ROLE_3', 'ROLE_4')") // 🔐 Role 3+

    public ResponseEntity<?> updateCharacter(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("gender") String gender,
            @RequestParam("description") String description,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "actor", required = false) String actorName,
            @RequestParam(value = "dabber", required = false) String dabberName,
            @RequestParam(value = "movies") String movies,
            @RequestParam(value = "quotes", required = false) String quotes,
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "picture", required = false) MultipartFile image
    ) {
        try {
            System.out.println("zde");
            // 🔹 Předzpracování
            List<String> movieNames = List.of(movies.split(";"));
            List<String> quoteList = new ArrayList<>();
            quoteList = quotes != null && !quotes.isEmpty() ? List.of(quotes.split(";")) : List.of();


            // 🔹 Vytvoření Character objektu
            Character updated = new Character();
            updated.setId(id);
            updated.setName(name);
            updated.setType(type);
            updated.setGender(gender);
            updated.setDescription(description);
            updated.setAge(age);
            updated.setNickname(nickname);

            if (actorName != null && !actorName.isEmpty()) {
                Actor actor = new Actor();
                actor.setName(actorName);
                updated.setActor(actor);
            }
            if (dabberName != null && !dabberName.isEmpty()) {
                Actor dabber = new Actor();
                dabber.setName(dabberName);
                updated.setDabber(dabber);
            }

            List<Movie> moviesList = new ArrayList<>();
            for(int i = 0; i < movieNames.size(); i++){
                Movie movie = new Movie();
                movie.setNameMovie(movieNames.get(i));  ;
                moviesList.add(movie);
            }
            updated.setMovies(moviesList);

            if (image != null && !image.isEmpty()) {
                updated.setImageBytes(image.getBytes());
            }

            User user = new User();
            user.setId(userId);

            characterService.updateCharacter(updated, user, quoteList);

            return ResponseEntity.ok().body("{\"message\":\"Character updated successfully\"}");

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"An error occurred while updating the character\"}");
        }
    }
    @Operation(summary = "Dashboard data", description = "Gets data for character dashboard. Can be used by users with role 3+.")
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyAuthority('ROLE_3', 'ROLE_4')") // 🔐 Role 3+
    public ResponseEntity<List<Character>> getDashboardData() {
        List<Character> characterList = characterService.getCharacters();  // přes service/DAO
        return ResponseEntity.ok(characterList);
    }

    @Operation(summary = "Gets similiar characters", description = "Gets characters from the same film as input character. Is used in You might like section in character detail.")
    @GetMapping("/{id}/similar")
    public List<Character> getSimilarCharacters(@PathVariable int id) {
        return characterService.getSimilarCharacters(id);
    }
}
