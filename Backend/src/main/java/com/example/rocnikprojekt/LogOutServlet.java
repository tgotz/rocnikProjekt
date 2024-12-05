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
@WebServlet(name = "LogOutServlet", value = "/api/logout")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);

        // Vynulování cookies
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);      // Okamžité vypršení
        cookie.setHttpOnly(true); // Stejné jako při přihlášení
        cookie.setSecure(false);  // Upravte na true, pokud používáte HTTPS
        cookie.setPath("/");      // Stejná cesta jako při přihlášení
        response.addCookie(cookie);

        // Odpověď
        response.getWriter().write("{\"message\":\"Logged out successfully\"}");
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
