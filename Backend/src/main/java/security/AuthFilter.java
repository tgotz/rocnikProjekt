package security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JwtUtil;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*"}) // Aplikujte na všechny endpointy začínající /api/
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicializace filtru (volitelné)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Nastavení CORS hlaviček
        setCorsHeaders(httpResponse);

        // Pokud je to OPTIONS požadavek, vrátíme OK a přerušíme řetězec
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Získání tokenu z cookies
        String token = JwtUtil.getTokenFromCookies(httpRequest);

        if (token != null && JwtUtil.validateToken(token)) {
            // Token je validní, přidáme atributy
            String username = JwtUtil.getUsernameFromToken(token);
            int role = JwtUtil.getRoleFromToken(token);

            // Nastavení atributů do žádosti
            httpRequest.setAttribute("username", username);
            httpRequest.setAttribute("role", role);

            // Pokračujeme v řetězci filtrů
            chain.doFilter(httpRequest, httpResponse);
        } else {
            // Token není validní
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"Unauthorized\"}");
        }
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    public void destroy() {
        // Čistící operace (volitelné)
    }
}
