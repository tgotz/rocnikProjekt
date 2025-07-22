package com.example.rocnikprojekt;

import dao.CharacterDAO;
import dao.QuotesDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet(name = "UpdateCharacterServlet", value = "/api/update-character")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class UpdateCharacterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            // checking if user should have access to this function
            Integer role = (Integer) request.getAttribute("role");
            if (role == null || role < 3) { // Role 3 = Moderátor nebo vyšší
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
                return;
            }

            // getting user id
            String userId = request.getParameter("userId");
            if (userId == null || userId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing user ID\"}");
                return;
            }
            User user = new User();
            user.setId(Integer.parseInt(userId));

            // getting parameters
            String movies = request.getParameter("movies");
            String[] moviesArray = movies != null ? movies.split(";") : new String[0];

            String id = request.getParameter("id");

            String name = request.getParameter("name");

            String type = request.getParameter("type");

            String gender = request.getParameter("gender");

            String desc = request.getParameter("desc");


            // checking required parameters
            if (id == null || id.isEmpty() ||
                    name == null || name.isEmpty() ||
                    type == null || type.isEmpty() ||
                    gender == null || gender.isEmpty() ||
                    desc == null || desc.isEmpty() ||
                    moviesArray.length == 0)
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing required fields\"}");
                return;
            }

            Character updatedCharacter = new Character();
            updatedCharacter.setId(Integer.parseInt(id));
            updatedCharacter.setName(name);
            updatedCharacter.setType(type);
            updatedCharacter.setGender(gender);
            updatedCharacter.setDesc(desc);


            // optional parameters
            if (request.getParameter("age") != null && !request.getParameter("age").isEmpty()) {
                updatedCharacter.setAge(Integer.parseInt(request.getParameter("age")));
            }
            if (request.getParameter("actor") != null && !request.getParameter("actor").isEmpty()) {
                updatedCharacter.setActorName(request.getParameter("actor"));
            }
            if (request.getParameter("dabber") != null && !request.getParameter("dabber").isEmpty()) {
                updatedCharacter.setDabberName(request.getParameter("dabber"));
            }
            if (request.getParameter("nickname") != null && !request.getParameter("nickname").isEmpty()) {
                updatedCharacter.setNickname(request.getParameter("nickname"));
            }

            //getting image
            Part filePart = request.getPart("picture");
            if(filePart != null){
                InputStream fileContent = filePart.getInputStream();
                byte[] imageBytes = fileContent.readAllBytes();
                updatedCharacter.setImageBytes(imageBytes);
                System.out.println("Image Bytes Length: " + imageBytes.length);
            }

            //movies
            updatedCharacter.setMovieList(List.of(moviesArray));

            //quotes
            String quotes = request.getParameter("quotes");
            String[] quotesArray = quotes != null ? quotes.split(";") : new String[0];

            CharacterDAO characterDAO = new CharacterDAO();
            characterDAO.updateCharacter(updatedCharacter, user, quotesArray);
            response.getWriter().write("{\"message\":\"Character updated successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"An error occurred while updating the character\"}");
        }
    }
}
