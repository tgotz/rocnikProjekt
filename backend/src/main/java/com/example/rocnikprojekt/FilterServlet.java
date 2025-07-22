package com.example.rocnikprojekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dao.CharacterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Character;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "filter", value = "/filter")
public class FilterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CharacterDAO characterDAO = new CharacterDAO();
        List<Character> characters = characterDAO.getCharacters();


        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), characters);

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
