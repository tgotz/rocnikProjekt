package com.example.backendspring.controller;

import com.example.backendspring.dto.AuthRequest;
import com.example.backendspring.dto.AuthResponse;
import com.example.backendspring.model.User;
import com.example.backendspring.repository.UserRepository;
import com.example.backendspring.config.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            // ✅ Autentizace
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // ✅ Načtení uživatele z DB
            Optional<User> userOpt = userRepository.findByUsername(authRequest.getUsername());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Uživatel neexistuje");
            }
            User user = userOpt.get();

            // ✅ Generování tokenu
            String token = jwtTokenProvider.generateToken(user);
            Cookie jwtCookie = new Cookie("token", token);
            jwtCookie.setHttpOnly(true); // ✅ Ochrana před JavaScript útoky
            jwtCookie.setSecure(false);  // ✅ Nastav `true`, pokud používáš HTTPS
            jwtCookie.setPath("/");      // ✅ Zajištění, že cookie je dostupná v celém API
            jwtCookie.setMaxAge(3600);   // ✅ Platnost 1 hodina

            response.addCookie(jwtCookie); // ✅ Odeslání cookie klientovi

                    // ✅ Vrácení odpovědi
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().getId(), user.getId()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Špatné přihlašovací údaje");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        System.out.println("odhlašuju");
        // ✅ Vytvoříme nový cookie s prázdnou hodnotou a okamžitou expirací
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0); // ⏳ Okamžitě expiruje
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ✅ Použij `true`, pokud používáš HTTPS
        cookie.setPath("/"); // 🌍 Globální cookie pro všechny endpointy

        response.addCookie(cookie);

        return ResponseEntity.ok("{\"message\":\"Logged out successfully\"}");
    }

}
