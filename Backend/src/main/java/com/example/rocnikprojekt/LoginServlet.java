package com.example.rocnikprojekt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CharacterDAO;
import dao.QuotesDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Character;
import model.User;
import util.JwtUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Nastavení hlaviček pro CORS
        setCorsHeaders(response);

        // Získání přihlašovacích údajů
        BufferedReader reader = request.getReader();
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }

        // Konverze JSON na mapu nebo objekt
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonMap = mapper.readValue(jsonString.toString(), new TypeReference<Map<String, String>>() {});

        String username = jsonMap.get("username");
        String password = jsonMap.get("password");


        // Ověření uživatele
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username);

        if (user != null && userDAO.verifyPassword(username, password)) {
            // Pokud je ověření správné, vytvoření JWT tokenu
            String token = JwtUtil.generateToken(user);
            Cookie jwtCookie = new Cookie("token", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/"); // Aby byla cookie přístupná pro všechny endpointy
            jwtCookie.setMaxAge(60 * 60); // Platnost 1 hodina
            response.addCookie(jwtCookie);

            // Vracení odpovědi
            System.out.println(user.getId());
            response.getWriter().write("{\"message\":\"Přihlášení bylo úspěšné\", \"username\":\"" + user.getName() + "\", \"role\":\"" + user.getRole() + "\", \"userId\":\"" + user.getId() + "\"}");
        } else {
            // Pokud ověření selže
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid username or password\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
