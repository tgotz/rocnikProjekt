package com.example.rocnikprojekt;

import dao.CharacterDAO;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.InputStream;
@WebServlet(name = "DeleteUserServlet", value = "/api/delete-user")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role < 4) { // Role 4 = Admin
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
            return;
        }

        String userId = request.getParameter("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing user ID\"}");
            return;
        }

        UserDAO userDAO = new UserDAO();
        userDAO.deleteUser(Integer.parseInt(userId));

        response.getWriter().write("{\"message\":\"User deleted successfully\"}");
    }
}
