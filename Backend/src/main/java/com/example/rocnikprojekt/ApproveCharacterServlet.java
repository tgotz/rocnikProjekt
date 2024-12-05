package com.example.rocnikprojekt;

import java.io.*;
import dao.CharacterDAO;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "approveCharacterServlet", value = "/api/approve-character")
public class ApproveCharacterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Nastavení hlaviček
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        try {
            // Ověření oprávnění uživatele
            Integer role = (Integer) request.getAttribute("role");
            if (role == null || role < 2) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
                return;
            }

            // Načtení dat z požadavku
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String requestBody = jsonBuilder.toString();

            // Parsování JSON dat
            com.google.gson.JsonObject json = new com.google.gson.JsonParser().parse(requestBody).getAsJsonObject();
            Integer characterId = json.get("id").getAsInt();
            Integer userId = json.get("userId").getAsInt();

            // Debug log
            System.out.println("Character ID: " + characterId);
            System.out.println("User ID: " + userId);

            // Zavolání metody pro schválení
            CharacterDAO characterDAO = new CharacterDAO();
            characterDAO.approveCharacter(characterId, userId);

            // Úspěšná odpověď
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\":\"Character approved successfully.\"}");

        } catch (Exception e) {
            // Zpracování chyby
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"An error occurred while approving the character.\"}");
        }
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
