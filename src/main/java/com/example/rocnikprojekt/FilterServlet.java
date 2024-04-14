package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Character;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "filter", value = "/filter")
public class FilterServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("page") != null){
            int pageNumber = Integer.parseInt(request.getParameter("page"));
            request.setAttribute("pageNumber", pageNumber);
        }

        CharacterDAO characterDAO = new CharacterDAO();
        ArrayList<Character> characterArrayList = characterDAO.getCharacters(request);
        for(int i = 0; i < characterArrayList.size(); i++){
        }
        request.setAttribute("charactersFiltred", characterDAO.getCharacters(request));
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

    }

    public void destroy() {
    }
}