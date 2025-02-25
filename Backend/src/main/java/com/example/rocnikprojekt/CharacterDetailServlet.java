package com.example.rocnikprojekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CharacterDAO;
import dao.QuotesDAO;
import dao.ReviewsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Character;
import model.Review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CharacterDetail", value = "/character")
public class CharacterDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();

        try {
            // checking if user should have access to this function
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                int id = Integer.parseInt(request.getParameter("id"));

                CharacterDAO characterDAO = new CharacterDAO();
                Character character = characterDAO.getCharacterDetail(id);

                ReviewsDAO reviewsDAO = new ReviewsDAO();
                ArrayList<Review> reviewList = reviewsDAO.getReviews(id);

                QuotesDAO quotesDAO = new QuotesDAO();
                ArrayList<String> quotesList = quotesDAO.getQuotes(id);

                responseData.put("character", character);
                responseData.put("reviews", reviewList);
                responseData.put("quotes", quotesList);
            } else {
                responseData.put("error", "ID postavy nebylo poskytnuto.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            responseData.put("error", "Došlo k chybě při zpracování požadavku.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

        // Převod odpovědi do JSON formátu
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseData);
    }
}
