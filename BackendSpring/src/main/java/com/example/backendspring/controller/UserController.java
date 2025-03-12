package com.example.backendspring.controller;

import com.example.backendspring.config.JwtTokenProvider;
import com.example.backendspring.model.Role;
import com.example.backendspring.model.User;
import com.example.backendspring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public UserController(JwtTokenProvider jwtTokenProvider , UserService userService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
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
    @PreAuthorize("hasAnyAuthority('ROLE_4', 'ROLE_3')")
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


    

}
