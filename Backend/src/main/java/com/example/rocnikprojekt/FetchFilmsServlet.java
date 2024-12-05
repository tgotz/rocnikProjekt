package com.example.rocnikprojekt;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.FilmDAO;
@WebServlet(name = "fetchFilms", value = "/fetchFilms")
public class FetchFilmsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        List<String> filmNames = new ArrayList<>();

        try {
            // Create an instance of FilmDAO
            FilmDAO filmDAO = new FilmDAO();

            // Call the non-static method on the instance
            filmNames = filmDAO.findFilmNames(input);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        // Convert filmNames to JSON and send it as response
        String json = new Gson().toJson(filmNames);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    public void destroy() {
    }
}