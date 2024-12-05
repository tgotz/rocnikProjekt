package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import model.User;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 3600 * 1000; // 1 hodina

    public static String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) { // Název cookie, který používáte pro uložení tokenu
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getName());
        claims.put("userId", user.getId()); // Přidání userId do tokenu
        claims.put("role", user.getRole()); // Přidání role do tokenu

        return Jwts.builder()
                .setSubject(user.getName())
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static Integer getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder() // Oprava: Použití parserBuilder místo parser
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("role", Integer.class); // Extrakce role
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        return (Integer) claims.get("userId"); // Získání userId z claims
    }
}
