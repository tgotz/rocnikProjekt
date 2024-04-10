package com.example.rocnikprojekt;

import dao.CharacterDAO;
import dao.QuotesDAO;
import dao.ReviewsDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Character;
import model.Review;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ChracterDetail", value = "/detail")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CharacterDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //checking if id was provided in url
        if(request.getParameter("id") != null && !request.getParameter("id").isEmpty()){
            //getting the id
            int id = Integer.parseInt(request.getParameter("id"));

            //getting all the details about character
            //getting the character detail - return is Character
            CharacterDAO characterDAO = new CharacterDAO();
            Character character = characterDAO.getCharacterDetail(id);
            //geting reviews
            ReviewsDAO reviewsDAO = new ReviewsDAO();
            ArrayList<Review> reviewList = reviewsDAO.getReviews(id);
            //getting character's quotes
            QuotesDAO quotesDAO = new QuotesDAO();
            ArrayList<String> quotesList = quotesDAO.getHlasky(id);
            System.out.println((quotesList.size()));
            request.setAttribute("character", character);
            request.setAttribute("reviewList", reviewList);
            request.setAttribute("quotesList", quotesList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/detail.jsp");
            dispatcher.forward(request, response);
        } else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);

        }



    }

    @Override
    public void destroy() {
        super.destroy();
    }
}