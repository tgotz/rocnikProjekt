package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Character;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@WebServlet(name = "ChracterDetail", value = "/detail")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CharacterDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //checking if id was provided in url
        if(request.getParameter("id") != null && !request.getParameter("id").isEmpty()){
            //getting the id
            int id = Integer.parseInt(request.getParameter("id"));

            //getting the character detail - return is Character
            try {
                CharacterDAO characterDAO = new CharacterDAO();
                characterDAO.getCharacterDetail(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }



    }

    @Override
    public void destroy() {
        super.destroy();
    }
}