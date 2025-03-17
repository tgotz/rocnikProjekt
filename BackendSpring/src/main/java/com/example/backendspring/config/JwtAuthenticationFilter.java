package com.example.backendspring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Zde");

        String token = jwtTokenProvider.getTokenFromCookies(request);
        System.out.println(token);
        if (token != null) {
            System.out.println("✅ Token nalezen v požadavku: " + token);

            try {
                // Validace tokenu
                if (jwtTokenProvider.validateToken(token)) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    Integer role = jwtTokenProvider.getRoleFromToken(token);

                    System.out.println("👤 Ověřený uživatel: " + username + " | Role: " + role);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 🔥 Převádíme roli (číslo) na GrantedAuthority
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    System.out.println(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println(SecurityContextHolder.getContext().getAuthentication());
                    System.out.println("✅ Uživatel nastaven do SecurityContextu: " + username);
                } else {
                    clearInvalidToken(response);
                    System.out.println("❌ Token je neplatný!");
                }
            } catch (SignatureException e) {
                // Pokud token není podepsán správně
                System.out.println("❌ Token není podepsán správně: " + e.getMessage());
                clearInvalidToken(response);  // Smazání nevalidního tokenu
            } catch (ExpiredJwtException e) {
                // Pokud token vypršel
                System.out.println("❌ Token vypršel: " + e.getMessage());
                clearInvalidToken(response);  // Smazání nevalidního tokenu
            } catch (Exception e) {
                // Pokud dojde k jiné chybě při validaci tokenu
                System.out.println("❌ Chyba při validaci tokenu: " + e.getMessage());
                clearInvalidToken(response);  // Smazání nevalidního tokenu
            }
        } else {
            System.out.println("❌ Token nenalezen v požadavku!");
        }

        // Pokračování ve filtru
        filterChain.doFilter(request, response);
    }

    private void clearInvalidToken(HttpServletResponse response) {
        // Smazání tokenu z cookies
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");  // Nastavení stejného path
        response.addCookie(cookie);
    }
}
