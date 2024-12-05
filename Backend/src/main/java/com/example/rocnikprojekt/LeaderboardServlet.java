package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import model.Character;

@WebServlet(name = "LeaderboardServlet", value = "/leaderboard")
public class LeaderboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Nastavení CORS hlaviček
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Parsování parametru "sort"
        int leaderBoardId = Integer.parseInt(request.getParameter("sort"));
        String orderBy;
        String headline;

        switch (leaderBoardId) {
            case 1:
                orderBy = "overall DESC";
                headline = "Nejoblíbenější postavy";
                break;
            case 2:
                orderBy = "overall";
                headline = "Nejnenáviděnější postavy";
                break;
            case 3:
                orderBy = "attractiveness DESC";
                headline = "Nejatraktivnější postavy";
                break;
            default:
                orderBy = "overall DESC";
                headline = "Nejoblíbenější postavy";
        }

        // Získání dat z DAO
        CharacterDAO characterDAO = new CharacterDAO();
        List<Character> characters = characterDAO.getCharactersLB(orderBy);

        // Sestavení odpovědi
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Vytvoření objektu pro JSON
        ObjectMapper mapper = new ObjectMapper();

        // Vrácení dat
        mapper.writeValue(response.getWriter(), characters);
    }
}
