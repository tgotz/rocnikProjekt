package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.InputStream;

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
            // Ověření oprávnění uživatele
            Integer role = (Integer) request.getAttribute("role");
            if (role == null || role < 3) { // Role 3 = Moderátor nebo vyšší
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\":\"Forbidden. Insufficient role.\"}");
                return;
            }

            // Získání ID uživatele
            String userId = request.getParameter("userId");
            if (userId == null || userId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing user ID\"}");
                return;
            }
            User user = new User();
            user.setId(Integer.parseInt(userId));

            // Získání parametrů
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String gender = request.getParameter("gender");
            String film = request.getParameter("film");
            String desc = request.getParameter("desc");

            // Kontrola povinných polí
            if (id == null || id.isEmpty() ||
                    name == null || name.isEmpty() ||
                    type == null || type.isEmpty() ||
                    gender == null || gender.isEmpty() ||
                    film == null || film.isEmpty() ||
                    desc == null || desc.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing required fields\"}");
                return;
            }

            // Sestavení postavy
            Character updatedCharacter = new Character();
            updatedCharacter.setId(Integer.parseInt(id));
            updatedCharacter.setName(name);
            updatedCharacter.setType(type);
            updatedCharacter.setGender(gender);
            //updatedCharacter.setFilmName(film);
            updatedCharacter.setDesc(desc);

            // Volitelné parametry
            String age = request.getParameter("age");
            if (age != null && !age.isEmpty()) {
                updatedCharacter.setAge(Integer.parseInt(age));
            }

            String actor = request.getParameter("actor");
            if (actor != null && !actor.isEmpty()) {
                updatedCharacter.setActorName(actor);
            }

            String nickname = request.getParameter("nickname");
            if (nickname != null && !nickname.isEmpty()) {
                updatedCharacter.setNickname(nickname);
            }

            // Zpracování obrázku
            Part filePart = request.getPart("picture");
            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileContent = filePart.getInputStream();
                updatedCharacter.setImageBytes(fileContent.readAllBytes());
            }

            // Aktualizace v databázi
            CharacterDAO characterDAO = new CharacterDAO();
            //characterDAO.updateCharacter(updatedCharacter, user);

            // Úspěšná odpověď
            response.getWriter().write("{\"message\":\"Character updated successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"An error occurred while updating the character\"}");
        }
    }
}
