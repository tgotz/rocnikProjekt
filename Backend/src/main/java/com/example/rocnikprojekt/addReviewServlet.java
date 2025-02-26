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
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        PrintWriter out = response.getWriter();
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
            out.print("{\"status\": \"success\", \"message\": \"Recenze byla úspěšně přidána!\", \"characterId\": " + review.getCharacterId() + "}");
            out.flush();
        } else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\": \"error\", \"message\": \"Chybí některé parametry!\"}");
            out.flush();
        }
    }
}