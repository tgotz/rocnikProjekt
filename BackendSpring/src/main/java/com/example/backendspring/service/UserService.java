package com.example.backendspring.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.backendspring.model.Role;
import com.example.backendspring.model.User;
import com.example.backendspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Získá uživatele podle jména
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getUserById(int id) {return userRepository.findById(id).orElse(null);}
    // Ověří heslo uživatele
    public boolean verifyPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword() != null) {
            return BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified;
        }
        return false;
    }

    // Smaže uživatele podle ID
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    // Aktualizuje roli uživatele
    public void updateUserRole(Integer userId, Role role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(role);
            userRepository.save(user);
        }
    }

    // Získá všechny uživatele
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
