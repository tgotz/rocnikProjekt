package com.example.rocnikprojekt;

import dao.CharacterDAO;
import dao.QuotesDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Character;
import model.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet(name = "login", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //getting user data from form
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    User user = new User();
    user.setName(username);
    user.setPassword(password);
    UserDAO userDAO = new UserDAO();
    //checking if the data are correct
    if(userDAO.verifyPassword(user.getName(), user.getPassword())) {
        // if correct
        HttpSession session = request.getSession();
        //saving user to session
        session.setAttribute("user", user);

        response.sendRedirect("approve");


    }else{
        request.setAttribute("error", "Nesprávné heslo nebo přihlašovací jméno");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    }

    public void destroy() {
    }
}