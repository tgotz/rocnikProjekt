package com.example.backendspring.controller;

import com.example.backendspring.dto.AuthRequest;
import com.example.backendspring.dto.AuthResponse;
import com.example.backendspring.model.User;
import com.example.backendspring.repository.UserRepository;
import com.example.backendspring.config.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

    @Operation(summary = "Logs user in.", description = "Logs user in. Can be used from log in page. Also checks if user has verified his email.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            // authentification
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // Getting user from DB
            Optional<User> userOpt = userRepository.findByUsername(authRequest.getUsername());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Uživatel neexistuje");
            }

            User user = userOpt.get();

            // Did user verify his email?
            if (!user.isVerified()) {
                return ResponseEntity.status(403).body(Map.of(
                        "error", "NOT_VERIFIED",
                        "message", "Účet není ověřený",
                        "email", user.getEmail(),
                        "username", user.getUsername(),
                        "password", user.getPassword()
                ));
            }


            // generating token
            String token = jwtTokenProvider.generateToken(user);
            Cookie jwtCookie = new Cookie("token", token);
            jwtCookie.setHttpOnly(true); //
            jwtCookie.setSecure(false);  // set true if https
            jwtCookie.setPath("/");      //
            jwtCookie.setMaxAge(3600);   // 1 hour expiricy

            response.addCookie(jwtCookie); //


            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().getId(), user.getId()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Špatné přihlašovací údaje");
        }
    }
    @Operation(summary = "Logs user out.", description = "Logs out user. Can be used from header - user details.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        System.out.println("odhlašuju");
        // Making a new cookie with no value
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0); // instant expiry
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set true if https
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok("{\"message\":\"Logged out successfully\"}");
    }

}
