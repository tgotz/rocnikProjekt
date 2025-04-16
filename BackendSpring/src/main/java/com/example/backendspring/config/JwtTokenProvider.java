package com.example.backendspring.config;

import com.example.backendspring.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Component
public class JwtTokenProvider {


    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 86400000; // 1 den

    private final UserDetailsService userDetailsService;
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())  // ✅ Nastavení subjektu (uživatelské jméno)
                .claim("role", user.getRole().getId())   // ✅ Přidání role
                .claim("userId", user.getId())   // ✅ Přidání userId
                .setIssuedAt(new Date())         // ✅ Issued At
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // ✅ Expiration
                .signWith(secretKey, SignatureAlgorithm.HS256) // ✅ Podepsání tokenu
                .compact();
    }

        public boolean validateToken(String token) {
            try {
                // getting username from tokn
                String username = getUsernameFromToken(token);
                if (username == null) {
                    return false;
                }

                // is token expired?
                if (isTokenExpired(token)) {
                    return false;
                }

                // token is valid
                return true;
            } catch (SignatureException e) {
                System.out.println("Token není podepsán správně: " + e.getMessage());
            } catch (ExpiredJwtException e) {
                System.out.println("Token vypršel: " + e.getMessage());
            } catch (MalformedJwtException e) {
                System.out.println("Token je poškozený: " + e.getMessage());
            } catch (UnsupportedJwtException e) {
                System.out.println("Token není podporován: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Token je prázdný nebo neplatný: " + e.getMessage());
            }
            return false; // token isn't valid
        }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // ✅ Opravená metoda
                .build()
                .parseClaimsJws(token)
                .getBody(); // ✅ Vrací `Claims` správně pro verzi `0.11.5`

        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    public Integer getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", Integer.class));
    }


    public Integer getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", Integer.class));
    }
    public String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) { // Hledáme cookie s názvem "token"
                    return cookie.getValue();
                }
            }
        }
        return null; // Pokud cookie neexistuje, vrátí null
    }
    public List<GrantedAuthority> getAuthoritiesFromRole(Integer role) {
        return switch (role) {
            case 1 -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            case 2 -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_REVIEWER"));
            case 3 -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_MODERATOR"));
            case 4 -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            default -> Collections.emptyList();
        };
    }
}
