package com.example.rocnikprojekt;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.CharacterDAO;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@WebServlet(name = "UpdateUserRoleServlet", value = "/api/update-user-role")
public class UpdateUserRoleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        System.out.println("dsotal jsem se sem");
        // checking if user should have access to this function
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role < 4) { // Role 4 = Admin
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
            return;
        }
        System.out.println("dsotal jsem se sem2");
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JsonObject json = JsonParser.parseString(requestBody).getAsJsonObject();

        String userId = json.get("userId").getAsString();
        String newRole = json.get("newRole").getAsString();

        System.out.println(userId);
        System.out.println(newRole);
        if (userId == null || newRole == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing required parameters\"}");
            return;
        }

        UserDAO userDAO = new UserDAO();
        System.out.println("Zde");
        userDAO.updateUserRole(Integer.parseInt(userId), Integer.parseInt(newRole));

        response.getWriter().write("{\"message\":\"User role updated successfully\"}");
    }
}
