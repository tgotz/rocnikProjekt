package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
@WebServlet(name = "DashboardSErvlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //if session doesnt exist, dont create new one.
        HttpSession session = request.getSession(false);
        //checking if user is logged in - otherwise - bye bye
        if (session != null && session.getAttribute("user") != null) {
            CharacterDAO characterDAO = new CharacterDAO();
            request.setAttribute("characterList", characterDAO.getCharacters(request));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/dashboard.jsp");
            dispatcher.forward(request, response);
        } else{
            response.sendRedirect("login.jsp");
        }
        }


}