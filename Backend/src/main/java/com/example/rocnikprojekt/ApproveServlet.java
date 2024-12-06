package com.example.rocnikprojekt;

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

        // Kontrola přihlášení a role
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role < 2) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
            return;
        }

        try {
            // Získání neschválených postav
            CharacterDAO characterDAO = new CharacterDAO();
            QuotesDAO quotesDAO = new QuotesDAO();
            List<Character> characters = characterDAO.getUnapprovedCharacters();

            // JSON odpověď
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("["); // Zahájení pole všech postav

            for (int i = 0; i < characters.size(); i++) {
                Character character = characters.get(i);
                jsonResponse.append("{"); // Zahájení objektu postavy

                // Přidání vlastností postavy
                jsonResponse.append("\"id\": ").append(character.getId()).append(",");
                jsonResponse.append("\"name\": ").append(character.getName() != null ? "\"" + escapeJson(character.getName()) + "\"" : null).append(",");
                jsonResponse.append("\"nickname\": ").append(character.getNickname() != null ? "\"" + escapeJson(character.getNickname()) + "\"" : null).append(",");
                jsonResponse.append("\"age\": ").append(character.getAge()).append(",");
                jsonResponse.append("\"gender\": ").append(character.getGender() != null ? "\"" + escapeJson(character.getGender()) + "\"" : null).append(",");
                jsonResponse.append("\"type\": ").append(character.getType() != null ? "\"" + escapeJson(character.getType()) + "\"" : null).append(",");
                jsonResponse.append("\"actorName\": ").append(character.getActorName() != null ? "\"" + escapeJson(character.getActorName()) + "\"" : null).append(",");
                jsonResponse.append("\"filmName\": ").append(character.getFilmName() != null ? "\"" + escapeJson(character.getFilmName()) + "\"" : null).append(",");
                jsonResponse.append("\"description\": ").append(character.getDesc() != null ? "\"" + escapeJson(character.getDesc()) + "\"" : null).append(",");
                jsonResponse.append("\"image\": \"")
                        .append(character.getImage()) // Už je Base64 string
                        .append("\",");

                // Přidání hlášek
                jsonResponse.append("\"quotes\": [");
                List<String> quotes = quotesDAO.getQuotes(character.getId());
                for (int j = 0; j < quotes.size(); j++) {
                    jsonResponse.append("\"").append(escapeJson(quotes.get(j))).append("\"");
                    if (j < quotes.size() - 1) {
                        jsonResponse.append(","); // Čárka pouze mezi hláškami, ne na konci
                    }
                }
                jsonResponse.append("]"); // Uzavření pole hlášek

                jsonResponse.append("}"); // Uzavření objektu postavy

                // Přidání čárky mezi objekty postav (kromě posledního objektu)
                if (i < characters.size() - 1) {
                    jsonResponse.append(",");
                }
            }

            jsonResponse.append("]"); // Uzavření pole všech postav


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
