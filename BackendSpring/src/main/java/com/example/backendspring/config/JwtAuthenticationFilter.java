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

        String token = jwtTokenProvider.getTokenFromCookies(request);
        System.out.println(token);
        if (token != null) {
            System.out.println("Token nalezen v požadavku: " + token);

            try {
                // Validation of token
                if (jwtTokenProvider.validateToken(token)) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    Integer role = jwtTokenProvider.getRoleFromToken(token);

                    System.out.println("👤 Ověřený uživatel: " + username + " | Role: " + role);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Int role to GrantedAuthority - for security  config
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    System.out.println(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println(SecurityContextHolder.getContext().getAuthentication());
                    System.out.println("Uživatel nastaven do SecurityContextu: " + username);
                } else {
                    clearInvalidToken(response);
                    System.out.println("Token je neplatný!");
                }
            } catch (SignatureException e) {
                // If token isn't signed correctly
                System.out.println("Token není podepsán správně: " + e.getMessage());
                clearInvalidToken(response);  // Deleting invalid token
            } catch (ExpiredJwtException e) {
                // if token is expired
                System.out.println("Token vypršel: " + e.getMessage());
                clearInvalidToken(response);
            } catch (Exception e) {
                // Different error
                System.out.println("Chyba při validaci tokenu: " + e.getMessage());
                clearInvalidToken(response);
            }
        } else {
            System.out.println("Token nenalezen v požadavku!");
        }

        // continuing filtering
        filterChain.doFilter(request, response);
    }

    private void clearInvalidToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");  
        response.addCookie(cookie);
    }
}
