package com.example.backendspring.service;

import com.example.backendspring.model.Actor;
import com.example.backendspring.repository.ActorRepository;
import com.example.backendspring.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private CharacterRepository characterRepository;

    // finds actor by name - if not found returns -1
    public int getActorId(String actorName) {
        Optional<Actor> actor = actorRepository.findByName(actorName);
        return actor.map(Actor::getId).orElse(-1);
    }

    //  if actor doesnt exixts - inserts new actor
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

    // deletes actor if isn't used by any character
    public void deleteActorIfNotUsed(int actorId) {
        boolean isUsedAsDabber = characterRepository.existsByDabberId(actorId);
        boolean isUsedAsActor = characterRepository.existsByActorId(actorId);
        System.out.println(isUsedAsDabber);
        System.out.println(isUsedAsActor);
        if (!isUsedAsDabber && !isUsedAsActor) {
            actorRepository.deleteById(actorId);
        }
    }

    public Actor findById(int id) {
        return actorRepository.findById(id).orElse(null);
    }
}
