package com.example.rocnikprojekt;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JwtUtil;

import java.io.IOException;

@WebServlet(name = "UserInfoServlet", value = "/user-info")
public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String token = JwtUtil.getTokenFromCookies(request);

        if (token != null && JwtUtil.validateToken(token)) {
            String username = JwtUtil.getUsernameFromToken(token);
            Integer role = JwtUtil.getRoleFromToken(token);
            Integer userId = JwtUtil.getUserIdFromToken(token);

            response.setContentType("application/json");
            System.out.println("Dekódované uživatelské jméno: " + username);
            System.out.println("Dekódovaná role: " + role);
            response.getWriter().write("{\"username\":\"" + username + "\", \"role\":\"" + role + "\", \"userId\":\"" + userId + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Nastaví HTTP status na 401
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }
    }
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
