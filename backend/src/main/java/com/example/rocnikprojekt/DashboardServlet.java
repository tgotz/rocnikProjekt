package com.example.rocnikprojekt;

import com.google.gson.Gson;
import dao.CharacterDAO;
import model.Character;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", value = "/api/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // checking if user should have access to this function
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role < 3) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
            return;
        } else{
            CharacterDAO characterDAO = new CharacterDAO();
            List<Character> characterList = characterDAO.getCharacters();
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(characterList);

            response.getWriter().write(jsonResponse);
        }

    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Nastavení CORS hlaviček pro preflight OPTIONS požadavky
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
