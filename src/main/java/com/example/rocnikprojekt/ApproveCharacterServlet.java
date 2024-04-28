package com.example.rocnikprojekt;

import java.io.*;

import dao.CharacterDAO;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

@WebServlet(name = "approveCharacterServlet", value = "/approve-character")
public class ApproveCharacterServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //if session doesnt exist, dont create new one.
        HttpSession session = request.getSession(false);
        //checking if user is logged in - otherwise - bye bye
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                int id = Integer.parseInt(request.getParameter("id"));
                CharacterDAO characterDAO = new CharacterDAO();
                characterDAO.approveCharacter(id, user);
            }
            response.sendRedirect("/approve?CharacterCount=" + request.getParameter("CharacterCount"));
        }else{
            response.sendRedirect("login.jsp");
        }
    }

    public void destroy() {
    }
}