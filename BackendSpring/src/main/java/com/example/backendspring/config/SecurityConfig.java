package com.example.backendspring.config;
import com.example.backendspring.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    //setting required roles for certain APIs
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) //
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/character", "/api/character/{id}", "/api/character/{id}/similar", "/api/character/add").permitAll()
                        .requestMatchers("/api/reviews/add").hasAnyAuthority("ROLE_1", "ROLE_2", "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/leaderboard").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/users/profile", "/api/users/send-otp", "/api/users/change-password", "/api/reports/add").hasAnyAuthority("ROLE_1", "ROLE_2", "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/auth/logout", "/api/users/user-info", "api/users/register", "api/users/verify-otp", "api/users/resend-otp").permitAll()
                        .requestMatchers("/api/character/approve").hasAnyAuthority("ROLE_2", "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/character/delete-character/**").hasAnyAuthority("ROLE_2", "ROLE_3", "ROLE_4")
                        .requestMatchers( "/api/character/dashboard").hasAnyAuthority( "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/character/update-character").hasAnyAuthority( "ROLE_3", "ROLE_4")
                        .requestMatchers("/api/users", "api/users/{id}", "api/users/{id}/role").hasAnyAuthority("ROLE_3", "ROLE_4")
                        .requestMatchers("/api/reports/solved", "/api/reports").hasAnyAuthority("ROLE_3", "ROLE_4")
                        .requestMatchers(
                                "/api/reports/admin/characters",
                                "/api/reports/admin/reviews",
                                "/api/reports/admin/character/**",
                                "/api/reports/admin/review/**",
                                "/api/reports/admin/resolve/character/**",
                                "/api/reports/admin/resolve/review/**"
                        ).hasAnyAuthority("ROLE_3", "ROLE_4")
                        .requestMatchers("/api/actors/**").permitAll()
                        .requestMatchers("/api/movies/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

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
