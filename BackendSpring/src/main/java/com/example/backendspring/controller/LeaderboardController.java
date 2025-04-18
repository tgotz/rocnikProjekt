package com.example.backendspring.controller;

import com.example.backendspring.dto.CharacterLeaderboardDTO;
import com.example.backendspring.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LeaderboardController {

    private final CharacterService characterService;

    public LeaderboardController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Operation(summary = "Gets leaderboard data.", description = "Gets leaderboard data.")
    @GetMapping
    public ResponseEntity<List<CharacterLeaderboardDTO>> getLeaderboard(@RequestParam(defaultValue = "overall DESC") String sort) {
        List<CharacterLeaderboardDTO> characters = characterService.getCharactersBySort(Integer.parseInt(sort));
        return ResponseEntity.ok(characters);
    }
}

