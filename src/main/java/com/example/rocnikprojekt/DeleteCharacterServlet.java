package com.example.rocnikprojekt;

import java.io.*;

import dao.CharacterDAO;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "deleteCharacterServlet", value = "/delete-character")
public class DeleteCharacterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //if session doesnt exist, dont create new one.
        HttpSession session = request.getSession(false);
        //checking if user is logged in - otherwise - bye bye
        if (session != null && session.getAttribute("user") != null) {

            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                int id = Integer.parseInt(request.getParameter("id"));
                CharacterDAO characterDAO = new CharacterDAO();
                characterDAO.deleteCharacter(id);

            }
            response.sendRedirect("/approve?CharacterCount=" + request.getParameter("CharacterCount"));
        }else{
            response.sendRedirect("login.jsp");
        }
    }

    public void destroy() {
    }
}