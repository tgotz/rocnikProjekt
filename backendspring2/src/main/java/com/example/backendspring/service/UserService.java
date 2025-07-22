package com.example.backendspring.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.backendspring.model.Role;
import com.example.backendspring.model.User;
import com.example.backendspring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getUserById(int id) {return userRepository.findById(id).orElse(null);}


    public boolean verifyPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword() != null) {
            return BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified;
        }
        return false;
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public void updateUserRole(Integer userId, Role role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(role);
            userRepository.save(user);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Map<String, Object> getUserProfile(int userId) {
        List<Object[]> resultList = userRepository.getUserProfile(userId);

        Map<String, Object> profile = new HashMap<>();
        if (resultList.isEmpty()) {
            throw new IllegalStateException("Neplatný výstup z getUserProfile pro userId: " + userId);
        }
        Object[] result = resultList.get(0);
        profile.put("id", result[0]);
        profile.put("username", result[1]);
        profile.put("email", result[2]);
        profile.put("roleId", result[3]);
        profile.put("registrationDate", result[4]);
        profile.put("roleName", result[5]);
        profile.put("characterCount", result[6]);
        profile.put("reviewCount", result[7]);

        return profile;
    }

    @Transactional
    public void changePassword(int userId, String oldPassword, String newPassword, String otp) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Uživatel nenalezen."));

        // checking old passowrd
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Staré heslo není správné.");
        }

        // checking otp
        if (!otpService.verifyOtp(user.getEmail(), otp)) {
            throw new IllegalArgumentException("Neplatný nebo expirovaný OTP kód.");
        }

        // setting up new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}
