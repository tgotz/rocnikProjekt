package com.example.rocnikprojekt;

import java.io.*;

import dao.CharacterDAO;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "approveCharacterServlet", value = "/approve-character")
public class ApproveCharacterServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("id") != null && !request.getParameter("id").isEmpty()){
            int id = Integer.parseInt(request.getParameter("id"));
            CharacterDAO characterDAO = new CharacterDAO();
            characterDAO.approveCharacter(id);
        }
        response.sendRedirect("/approve?CharacterCount=" + request.getParameter("CharacterCount"));

    }

    public void destroy() {
    }
}