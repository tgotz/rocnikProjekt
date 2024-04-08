package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;

import jakarta.servlet.annotation.MultipartConfig;

import model.Character;

@WebServlet(name = "AddCharacter", value = "/AddCharacterServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class AddCharacterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String gender = request.getParameter("gender");
        String film = request.getParameter("film");
        String desc = request.getParameter("desc");
        String quotes = request.getParameter("quotes");
        Part filePart = request.getPart("picture");

        //checking if all needed values are provided
        if(name != null && !name.isEmpty() && type != null && !type.isEmpty() && gender != null && !gender.isEmpty() && film != null && !film.isEmpty() && desc != null && !desc.isEmpty() && filePart != null){


        //creating a new character and providing the values
        Character newCharacter = new Character();
        newCharacter.setName(request.getParameter("name"));
        newCharacter.setType(request.getParameter("type"));
        newCharacter.setGender(request.getParameter("gender"));
        newCharacter.setFilmName(request.getParameter("film"));
        newCharacter.setDesc(request.getParameter("desc"));

        //checking if (optional) age was provided
        if(request.getParameter("age") != null && !request.getParameter("age").isEmpty()){
            newCharacter.setAge(Integer.parseInt(request.getParameter("age")));
        }
        newCharacter.setActorName(request.getParameter("actor"));

            //checking if (optional) nickname was provided
            if(request.getParameter("nickname") != null && !request.getParameter("nickname").isEmpty()){
            newCharacter.setNickname(request.getParameter("nickname"));
        }
        //converting the image into "byte []" - so I can store in the DB
        InputStream fileContent = filePart.getInputStream();
        newCharacter.setImage(fileContent.readAllBytes());

        //making array out of "quotes string" from form
        String[] quotesArray = request.getParameter("quotes").split(";");
        try {
            CharacterDAO characterDAO = new CharacterDAO();
            characterDAO.addCharacter(newCharacter, quotesArray);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("outcome", "Postava byla úspěšně přidána!");

        } else{
        //something wasnt provided
            request.setAttribute("outcome", "Nezadal jsi nějaké údaje, postava přidána nebyla.");
        }
        getServletContext().getRequestDispatcher("/add_character.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}