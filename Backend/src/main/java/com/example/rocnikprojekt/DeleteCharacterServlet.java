package com.example.rocnikprojekt;

import java.io.*;

import dao.CharacterDAO;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
@WebServlet(name = "deleteCharacterServlet", value = "/api/delete-character")
public class DeleteCharacterServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // Povolit doménu frontendu
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE"); // Povolit metody
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Povolit odesílání cookies

System.out.println("zde3");
        // Kontrola přihlášení a role
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role < 3) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
            return;
        } else{
            CharacterDAO characterDAO = new CharacterDAO();
            Integer characterId = Integer.parseInt(request.getParameter("id"));
            System.out.println(characterId);
            characterDAO.deleteCharacter(characterId);
            System.out.println("ZDE");
        }
    }

    @Override
    public void destroy() {
    }
}
