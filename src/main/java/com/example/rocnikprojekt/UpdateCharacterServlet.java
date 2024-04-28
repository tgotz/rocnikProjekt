package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Character;
import jakarta.servlet.annotation.MultipartConfig;
import model.User;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
@WebServlet(name = "UpdateCharacterServlet", value = "/UpdateCharacter")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class UpdateCharacterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //if session doesnt exist, dont create new one.
        HttpSession session = request.getSession(false);
        //checking if user is logged in - otherwise - bye bye
        if (session != null && session.getAttribute("user") != null) {
        int id = Integer.parseInt(request.getParameter("id"));
        CharacterDAO characterDAO = new CharacterDAO();
        request.setAttribute("character", characterDAO.getCharacterDetail(id));
        request.getRequestDispatcher("edit.jsp").forward(request, response);

            } else{
            response.sendRedirect("login.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //if session doesnt exist, dont create new one.
        HttpSession session = request.getSession(false);
        //checking if user is logged in - otherwise - bye bye
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
        }
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String gender = request.getParameter("gender");
        String film = request.getParameter("film");
        String desc = request.getParameter("desc");
        //checking if all needed values are provided
        if(name != null && !name.isEmpty() && type != null && !type.isEmpty() && gender != null && !gender.isEmpty() && film != null && !film.isEmpty() && desc != null && !desc.isEmpty()){
            //creating a new character and providing the values
            Character newCharacter = new Character();
            newCharacter.setId(Integer.parseInt(request.getParameter("id")));
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
            //checking if image was uploaded
            Part filePart = request.getPart("picture");
            if(filePart != null && filePart.getSize() > 0){
                //converting the image into "byte []" - so I can store in the DB
                InputStream fileContent = filePart.getInputStream();
                newCharacter.setImage(fileContent.readAllBytes());

            }
            User user = (User) session.getAttribute("user");
            CharacterDAO characterDAO = new CharacterDAO();
            characterDAO.updateCharacter(newCharacter, user);
            response.sendRedirect("detail?id="+newCharacter.getId());
        }
    }


    }