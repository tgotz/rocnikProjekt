package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // Nastavíme CORS hlavičky pro podporu API
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> jsonResponse = new HashMap<>();

        try {
            // Načteme povinné parametry
            String name = request.getParameter("name");
            System.out.println(name);
            String type = request.getParameter("type");
            System.out.println(type);
            String gender = request.getParameter("gender");
            System.out.println(gender);
            String film = request.getParameter("movies");
            System.out.println(film);
            String desc = request.getParameter("desc");
            System.out.println(desc);
            Part filePart = request.getPart("picture");
            System.out.println("File Part: " + filePart);

            // Zkontrolujeme, zda jsou povinné údaje zadány
            if (name == null || name.isEmpty() ||
                    type == null || type.isEmpty() ||
                    gender == null || gender.isEmpty() ||
                    film == null || film.isEmpty() ||
                    desc == null || desc.isEmpty() ||
                    filePart == null) {

                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Některé povinné údaje nebyly zadány.");
                response.getWriter().write(new com.google.gson.Gson().toJson(jsonResponse));
                return;
            }

            // Vytvoříme novou postavu
            Character newCharacter = new Character();
            newCharacter.setName(name);
            newCharacter.setType(type);
            newCharacter.setGender(gender);
            newCharacter.setDesc(desc);

            // Volitelné údaje
            if (request.getParameter("age") != null && !request.getParameter("age").isEmpty()) {
                newCharacter.setAge(Integer.parseInt(request.getParameter("age")));
            }
            if (request.getParameter("actor") != null && !request.getParameter("actor").isEmpty()) {
                newCharacter.setActorName(request.getParameter("actor"));
            }
            if (request.getParameter("dabber") != null && !request.getParameter("dabber").isEmpty()) {
                newCharacter.setDabberName(request.getParameter("dabber"));
            }
            if (request.getParameter("nickname") != null && !request.getParameter("nickname").isEmpty()) {
                newCharacter.setNickname(request.getParameter("nickname"));
            }

            // Obrazek (převedeme na byte[])
            InputStream fileContent = filePart.getInputStream();
            byte[] imageBytes = fileContent.readAllBytes();
            newCharacter.setImageBytes(imageBytes);
            System.out.println("Image Bytes Length: " + imageBytes.length);


            // Filmy
            String movies = request.getParameter("movies");
            String[] moviesArray = movies != null ? movies.split(";") : new String[0];
            newCharacter.setMovieList(List.of(moviesArray));
            // Hlášky
            String quotes = request.getParameter("quotes");
            String[] quotesArray = quotes != null ? quotes.split(";") : new String[0];

            // Uložíme postavu do databáze
            CharacterDAO characterDAO = new CharacterDAO();
            characterDAO.addCharacter(newCharacter, quotesArray);

            // Úspěšná odpověď
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Postava byla úspěšně přidána!");
        } catch (Exception e) {
            // Zpracování chyb
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Nastala chyba při přidávání postavy.");
        }

        // Odeslání odpovědi
        response.getWriter().write(new com.google.gson.Gson().toJson(jsonResponse));
    }
}
