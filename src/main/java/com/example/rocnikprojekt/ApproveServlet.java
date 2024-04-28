package com.example.rocnikprojekt;

import dao.CharacterDAO;
import dao.QuotesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Character;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "approve", value = "/approve")
public class ApproveServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //if session doesnt exist, dont create new one.
        HttpSession session = request.getSession(false);
        //checking if user is logged in - otherwise - bye bye
        if (session != null && session.getAttribute("user") != null) {

            int characterCount;

            if (request.getParameter("CharacterCount") != null) {
                characterCount = Integer.parseInt(request.getParameter("CharacterCount"));
            } else {
                characterCount = 0;
            }
            //setting attribute for selecting "unapproved" characters only
            request.setAttribute("unapproved", true);
            //getting the characters detail as attribute
            CharacterDAO characterDAO = new CharacterDAO();
            QuotesDAO quotesDAO = new QuotesDAO();
            ArrayList<Character> characters = characterDAO.getCharacters(request);

            //checking for case - if LAST character gets deleted/approved you cant redirect to the "next character" because there is none. So just redirect to the last one.
            //only happends if you're deleting/approving the last character
            if (characterCount >= characters.size()) {
                characterCount = characters.size() - 1;
            }
            if(characterCount >= 0){
                //character to be displayed
                request.setAttribute("character", characters.get(characterCount));
                // characters quotes
                request.setAttribute("quoteList", quotesDAO.getQuotes(characters.get(characterCount).getId()));
                //character count (for "paging" purpose)
                request.setAttribute("characterCount", characterCount);
                request.setAttribute("maxCharacterCount", characters.size());
            }

            request.getRequestDispatcher("approve.jsp").forward(request, response);
        }else{
            response.sendRedirect("login.jsp");
        }
    }

    public void destroy() {
    }
}