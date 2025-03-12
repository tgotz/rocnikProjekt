package com.example.backendspring.service;

import com.example.backendspring.model.Actor;
import com.example.backendspring.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    // 🔹 Najde ID herce podle jména, pokud existuje, jinak vrátí -1
    public int getActorId(String actorName) {
        Optional<Actor> actor = actorRepository.findByName(actorName);
        return actor.map(Actor::getId).orElse(-1);
    }

    // 🔹 Vloží herce, pokud ještě neexistuje, a vrátí jeho ID
    public int insertActor(String actorName) {
        Optional<Actor> existingActor = actorRepository.findByName(actorName);
        if (existingActor.isPresent()) {
            return existingActor.get().getId();
        }

        Actor newActor = new Actor();
        newActor.setName(actorName);
        actorRepository.save(newActor);
        return newActor.getId();
    }

    // 🔹 Smaže herce, pokud není použit v žádné postavě
    public void deleteActorIfNotUsed(int actorId) {
        actorRepository.deleteById(actorId);
    }
    // Přidáme metodu findById
    public Actor findById(int id) {
        return actorRepository.findById(id).orElse(null);
    }
}
