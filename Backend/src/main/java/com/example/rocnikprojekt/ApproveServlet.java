package com.example.rocnikprojekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CharacterDAO;
import dao.QuotesDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "approve", value = "/api/approve")
public class ApproveServlet extends HttpServlet {
    private String escapeJson(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
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
        if (role == null || role < 2) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
            return;
        }

        try {
            // getting not approved characters
            CharacterDAO characterDAO = new CharacterDAO();
            QuotesDAO quotesDAO = new QuotesDAO();
            List<Character> characters = characterDAO.getUnapprovedCharacters();

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("[");

            for (int i = 0; i < characters.size(); i++) {
                Character character = characters.get(i);
                jsonResponse.append("{");

                jsonResponse.append("\"id\": ").append(character.getId()).append(",");
                jsonResponse.append("\"name\": ").append(character.getName() != null ? "\"" + escapeJson(character.getName()) + "\"" : null).append(",");
                jsonResponse.append("\"nickname\": ").append(character.getNickname() != null ? "\"" + escapeJson(character.getNickname()) + "\"" : null).append(",");
                jsonResponse.append("\"age\": ").append(character.getAge()).append(",");
                jsonResponse.append("\"gender\": ").append(character.getGender() != null ? "\"" + escapeJson(character.getGender()) + "\"" : null).append(",");
                jsonResponse.append("\"type\": ").append(character.getType() != null ? "\"" + escapeJson(character.getType()) + "\"" : null).append(",");
                jsonResponse.append("\"actorName\": ").append(character.getActorName() != null ? "\"" + escapeJson(character.getActorName()) + "\"" : null).append(",");
                jsonResponse.append("\"dabberName\": ").append(character.getDabberName() != null ? "\"" + escapeJson(character.getDabberName()) + "\"" : null).append(",");
                jsonResponse.append("\"movieList\": [");
                List<String> movieList = character.getMovieList();
                for (int k = 0; k < movieList.size(); k++) {
                    jsonResponse.append("\"").append(escapeJson(movieList.get(k))).append("\"");
                    if (k < movieList.size() - 1) {
                        jsonResponse.append(",");
                    }
                }
                jsonResponse.append("],");
                jsonResponse.append("\"description\": ").append(character.getDesc() != null ? "\"" + escapeJson(character.getDesc()) + "\"" : null).append(",");
                jsonResponse.append("\"image\": \"")
                        .append(character.getImage())
                        .append("\",");

                jsonResponse.append("\"quotes\": [");
                List<String> quotes = quotesDAO.getQuotes(character.getId());
                for (int j = 0; j < quotes.size(); j++) {
                    jsonResponse.append("\"").append(escapeJson(quotes.get(j))).append("\"");
                    if (j < quotes.size() - 1) {
                        jsonResponse.append(",");
                    }
                }
                jsonResponse.append("]");

                jsonResponse.append("}");

                if (i < characters.size() - 1) {
                    jsonResponse.append(",");
                }
            }

            jsonResponse.append("]");

            // Odeslání odpovědi
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"An error occurred while fetching characters.\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
