package com.example.backendspring.config;
import com.example.backendspring.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Povolení CORS
                .csrf(csrf -> csrf.disable()) // ❌ Vypnout CSRF pro REST API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 📌 REST API je bezstavové                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/character", "/api/character/{id}", "api/character/add").permitAll()  // ✅ Veřejné endpointy
                        .requestMatchers("/api/reviews/add").permitAll()  // ✅ Přidávání recenzí je veřejné
                        .requestMatchers("/api/leaderboard").permitAll()
                        .requestMatchers("/api/auth/login").permitAll() // Přihlášení a user-info jsou veřejné
                        .requestMatchers("/api/auth/logout", "/api/users/user-info").permitAll()
                        .requestMatchers("/api/character/approve").hasAnyAuthority("ROLE_2", "ROLE_3", "ROLE_4") // 🔒 Přístup jen pro moderátory+
                        .requestMatchers("/api/character/delete-character/**").hasAnyAuthority("ROLE_2", "ROLE_3", "ROLE_4") // 🔥 Pouze ADMIN může mazat postavy
                        .requestMatchers( "/api/character/dashboard").hasAnyAuthority( "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/character/update-character").hasAnyAuthority( "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/users", "api/users/{id}", "api/users/{id}/role").hasAnyAuthority("ROLE_3", "ROLE_4")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // ✅ Povolit frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
