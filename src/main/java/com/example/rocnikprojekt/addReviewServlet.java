package com.example.rocnikprojekt;

import dao.ReviewsDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Review;

import java.io.*;

@WebServlet(name = "addReviewServlet", value = "/addReviewServlet")
public class addReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Review review = new Review();
        //checks if are any parametr empty
        if(request.getParameter("name") != null && !request.getParameter("name").isEmpty() && request.getParameter("reviewText") != null && !request.getParameter("reviewText").isEmpty() && request.getParameter("overallRating") != null && !request.getParameter("overallRating").isEmpty() && request.getParameter("attractivenessRating") != null && !request.getParameter("attractivenessRating").isEmpty()){
            review.setAuthorName(request.getParameter("name"));
            review.setReviewText(request.getParameter("reviewText"));
            review.setCharacterId(Integer.parseInt(request.getParameter("characterId")));
            review.setOverallRating(Integer.parseInt(request.getParameter("overallRating")));
            review.setAttractivenessRating(Integer.parseInt(request.getParameter("attractivenessRating")));
            ReviewsDAO reviewsDAO = new ReviewsDAO();
            reviewsDAO.insertReview(review);
            request.setAttribute("outcome", "Recenze byla úspěšně přidána!");
            response.sendRedirect("/detail?id=" + review.getCharacterId());
        } else{
            response.sendRedirect("/detail?id=" + review.getCharacterId());
        }
    }
}