package com.example.backendspring.controller;

import com.example.backendspring.config.JwtTokenProvider;
import com.example.backendspring.model.Role;
import com.example.backendspring.model.User;
import com.example.backendspring.repository.UserRepository;
import com.example.backendspring.service.OtpService;
import com.example.backendspring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;


    public UserController(JwtTokenProvider jwtTokenProvider , UserService userService, UserRepository userRepository, OtpService otpService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtTokenProvider.getTokenFromCookies(request);


        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Integer role = jwtTokenProvider.getRoleFromToken(token);
            Integer userId = jwtTokenProvider.getUserIdFromToken(token);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", username);
            userInfo.put("role", role);
            userInfo.put("userId", userId);

            return ResponseEntity.ok(userInfo);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unauthorized");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(errorResponse);
        }
    }
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_4')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PutMapping("/{id}/role")
    @PreAuthorize("hasAnyAuthority('ROLE_4', 'ROLE_3')")
    public ResponseEntity<String> updateUserRole(@PathVariable int id, @RequestParam int roleId) {
        Role role = new Role();
        role.setId(roleId);
        userService.updateUserRole(id, role);
        return ResponseEntity.ok("Role uživatele byla úspěšně aktualizována.");
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_4', 'ROLE_3')")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Uživatel byl úspěšně smazán.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email už existuje.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username už existuje.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(false);
        Role role = new Role();
        role.setId(1);
        user.setRole(role);

        userRepository.save(user);
        otpService.generateAndSendOtp(email);

        return ResponseEntity.ok("Registrace proběhla. Zkontroluj e-mail a zadej OTP kód.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpCode = request.get("otpCode");

        boolean isValid = otpService.verifyOtp(email, otpCode);

        if (!isValid) {
            return ResponseEntity.badRequest().body("Neplatný nebo expirovaný OTP kód.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen"));

        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("Účet ověřen. Můžeš se přihlásit.");
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok(Map.of("message", "Ověřovací kód byl znovu odeslán"));
    }


}
