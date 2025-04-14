
package com.example.backendspring.controller;

import com.example.backendspring.model.Actor;
import com.example.backendspring.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/search")
    public List<Actor> searchActors(@RequestParam String query) {
        return actorRepository.findByNameContainingIgnoreCase(query);
    }
}
