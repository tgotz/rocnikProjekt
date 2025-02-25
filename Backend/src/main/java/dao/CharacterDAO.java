package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import model.Character;
import model.User;

import javax.swing.plaf.nimbus.State;

public class CharacterDAO {
    //change if database details change
    private Connection connection;

    public CharacterDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
   /* public void updateCharacter(Character character, User user){
        ActorDAO actorDAO = new ActorDAO();
        FilmDAO filmDAO = new FilmDAO();

        //checking if actor and film already exists
        //if yes - gettings their ID
        //if not - inserting them and getting their ID
        int actorId = actorDAO.getActorId(character.getActorName());
        if(actorId == -1){
            actorId = actorDAO.insertActor(character.getActorName());
        }

        int filmId = filmDAO.getFilmId(character.getFilmName());
        if(filmId == -1){
            filmId = filmDAO.insertFilm(character.getFilmName());
        }

        //building sql query based on what "values" were provided- nickname, image and age are optional.
        String query = "UPDATE postavy SET jmeno = ?, popis = ?, datumPridani = NOW(), typPostavy = ?, pohlavi = ?, idherce = ?, iduser = ?, idfilmu = ?";

        //checking if age != default value (999)
        if(character.getAge() != 999){
            query += ", vek = ?";

        }
        //checking if nickname was provided
        if(character.getNickname() != null){
            query += ", prezdivka = ?";
        }
        //checking if Image was provided
        if(character.getImageBytes() != null){
            query += ", obrazek = ?";
        }
        //finishing the query
        query += " WHERE idpostavy = ?";

        //counter for statements
        int i = 8;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            System.out.println(statement);
            statement.setString(1, character.getName());
            statement.setString(2, character.getDesc());
            statement.setString(3, character.getType());
            statement.setString(4, character.getGender());
            statement.setInt(5, actorId);
            statement.setInt(6, user.getId());
            statement.setInt(7, filmId);
            if(character.getAge() != 999){
            statement.setInt(i, character.getAge());
            i++;
            }
            if(character.getNickname() != null){

                statement.setString(i, character.getNickname());
                i++;
            }
            if(character.getImageBytes() != null){

                statement.setBytes(i, character.getImageBytes());
                i++;
            }

            statement.setInt(i, character.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
    //method to add character
    public void addCharacter(Character character, String[] quotesArray){
        ActorDAO actorDAO = new ActorDAO();
        FilmDAO filmDAO = new FilmDAO();


        //checking if actor and film already exists
        //if yes - gettings their ID
        //if not - inserting them and getting their ID
        int actorId = -1;
        if(character.getActorName() != null) {
                actorId = actorDAO.getActorId(character.getActorName());
            if (actorId == -1) {
                actorId = actorDAO.insertActor(character.getActorName());
            }
        }
        int dabberId = -1;
        if(character.getDabberName() != null) {
               dabberId = actorDAO.getActorId(character.getDabberName());
            if (dabberId == -1) {
                dabberId = actorDAO.insertActor(character.getDabberName());
            }
        }

        List<Integer> movieIds = new ArrayList<>();
        for(int i = 0; i < character.getMovieList().size(); i++){
           movieIds.add(filmDAO.getFilmId(character.getMovieList().get(i)));
        if(movieIds.get(i) == -1){
            movieIds.remove(i);
            movieIds.add(filmDAO.insertFilm(character.getMovieList().get(i)));
        }
        }
        //building sql query based on what "values" were provided- nickname and age are optional.
        String sqlPart1 = "INSERT INTO characters (name, description, dateAdded, type, gender, picture";
        String values = "(?, ?, NOW(), ?, ?, ?";

        //checking if age != default value (999)
        if(character.getAge() != 999){
            sqlPart1 += ", age";
            values += ", ?";
        }
        //checking if nickname was provided
        if(character.getNickname() != null){
            sqlPart1 += ", nickname";
            values += ", ?";
        }
        //checking if actor was provided
        if(character.getActorName() != null){
            sqlPart1 += ", idActor";
            values += ", ?";
        }
        //checking if actor was provided
        if(character.getDabberName() != null){
            sqlPart1 += ", idDabber";
            values += ", ?";
        }
        //finishing the query
        sqlPart1 += ")";
        values += ")";
        String query = sqlPart1 + " VALUES " + values;
        int count = 6;
        try {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1,character.getName());
            statement.setString(2,character.getDesc());
            statement.setString(3,character.getType());
            statement.setString(4,character.getGender());
            statement.setBytes(5,character.getImageBytes());
            if(character.getAge() != 999){
                statement.setInt(count, character.getAge());
                count++;
            }
            if(character.getNickname() != null){
                    statement.setString(count, character.getNickname());
                    count++;
            }
            if(character.getActorName() != null){
                statement.setInt(count, actorId);
                count++;
            }
            if(character.getDabberName() != null){
                statement.setInt(count, dabberId);
                count++;
            }

            statement.executeUpdate();

            //geting id of the new added character
            ResultSet resultSet = statement.getGeneratedKeys();
            int idPostavy = -1;
            if(resultSet.next()){
                idPostavy = resultSet.getInt(1);
            }
            //adding the quotes if there are any quotes
            QuotesDAO quotesDAO = new QuotesDAO();
            if(idPostavy != -1){
                for(int i = 0; i < quotesArray.length; i++){
                    if(quotesArray[i] != null && !quotesArray[i].isEmpty()){
                        quotesDAO.insertQuote(quotesArray[i], idPostavy);
                    }
                }
            }
            filmDAO = new FilmDAO();
            if(idPostavy != -1){
                for(int i = 0; i < movieIds.size(); i++){

                    filmDAO.assignFilm(movieIds.get(i), idPostavy);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    //method to get character's details - for detail jsp
    public Character getCharacterDetail(int id){
        String query = "SELECT c.idCharacter, c.name, c.picture, c.type, c.gender,c.age, c.dateAdded, u.username AS addedBy, admin.username AS approvedBy, actor.name AS actorName, dabber.name AS dabberName, GROUP_CONCAT(m.nameMovie SEPARATOR ', ') AS movies FROM characters c LEFT JOIN users u ON c.addedBy = u.idUser LEFT JOIN  users admin ON c.approvedBy = admin.idUser LEFT JOIN actors actor ON c.idActor = actor.idActor LEFT JOIN actors dabber ON c.idDabber = dabber.idActor LEFT JOIN characters_movies cm ON c.idCharacter = cm.idCharacter LEFT JOIN movies m ON cm.idMovie = m.idMovie WHERE c.idCharacter = ? GROUP BY c.idCharacter, c.name, c.picture, c.type, c.gender, c.dateAdded, u.username, admin.username, actor.name, dabber.name ";
        Character character = new Character();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                character.setId(resultSet.getInt("idCharacter"));
                character.setName(resultSet.getString("name"));
                byte[] imageBytes = resultSet.getBytes("picture");
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    character.setImage(base64Image); // Nastav Base64 obrázek
                }
                character.setAge(resultSet.getInt("age"));
                character.setType(resultSet.getString("type"));
                character.setGender(resultSet.getString("gender"));
                character.setDateAdded(resultSet.getDate("dateAdded"));
                character.setAddedBy(resultSet.getString("addedBy"));
                character.setApprovedBy(resultSet.getString("approvedBy"));

                String actorName = resultSet.getString("actorName");
                if(actorName != null){
                    character.setActorName(actorName);
                }
                String dabberName = resultSet.getString("dabberName");
                if(dabberName != null){
                    character.setDabberName(dabberName);
                }
                String moviesStr = resultSet.getString("movies");
                List<String> movieList = new ArrayList<>();
                if (moviesStr != null && !moviesStr.isEmpty()) {
                    // Rozdělíme řetězec podle čárky a případně odstraníme mezery
                    movieList = new ArrayList<>(Arrays.asList(moviesStr.split(",\\s*")));
                }
                character.setMovieList(movieList);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return character;
    };
    //method to get characters for Leaderboards
    public ArrayList<Character> getCharactersLB(String orderBy){
        String query = "SELECT c.idCharacter as id, c.name as name, c.nickname as nickname, d.name as dabberName, a.name as actorName,GROUP_CONCAT(m.nameMovie SEPARATOR ', ') AS movies, AVG(reviews.overalRating) as overall, AVG(reviews.attractivenessRating) as attractiveness\n" +
                "FROM characters c\n" +
                "left join actors d on d.idActor = c.idDabber\n" +
                "left join actors a on a.idActor = c.idActor\n" +
                "join reviews on reviews.idCharacter = c.idCharacter\n" +
                "LEFT JOIN characters_movies cm ON c.idCharacter = cm.idCharacter LEFT JOIN movies m ON cm.idMovie = m.idMovie\n" +
                "GROUP BY c.idCharacter " +
                "ORDER BY " + orderBy;
        ArrayList<Character> characterList = new ArrayList<Character>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()){
                if(i < 50){
                    Character character = new Character();
                    character.setId(resultSet.getInt("id"));
                    character.setName(resultSet.getString("name"));
                    character.setNickname(resultSet.getString("nickname"));
                    character.setOverallRating(resultSet.getDouble("overall"));
                    character.setAttractivenessRating(resultSet.getDouble("attractiveness"));
                    String actorName = resultSet.getString("actorName");
                    if(actorName != null){
                        character.setActorName(actorName);
                    }
                    String dabberName = resultSet.getString("dabberName");
                    if(dabberName != null){
                        character.setDabberName(dabberName);
                    }
                    String moviesStr = resultSet.getString("movies");
                    List<String> movieList = new ArrayList<>();
                    if (moviesStr != null && !moviesStr.isEmpty()) {
                        // Rozdělíme řetězec podle čárky a případně odstraníme mezery
                        movieList = new ArrayList<>(Arrays.asList(moviesStr.split(",\\s*")));
                    }
                    character.setMovieList(movieList);
                    characterList.add(character);
                    i++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characterList;
    }
    //method to get recently added characters for index.jsp - recent characters part
    public ArrayList<Character> getRecentCharacters(){
        System.out.println("použivam recentCharacter");
        ArrayList<Character> characters = new ArrayList<Character>();
        /*
        try {
            Statement statement = connection.createStatement();

            //getting characters from the database for the "recently added" section
            String recentlyAddedQuery = "SELECT postavy.idpostavy as id, postavy.jmeno as jmeno, postavy.obrazek as obrazek, filmy.nazevFilmu as nazevFilmu FROM postavy JOIN filmy ON filmy.idfilmu = postavy.idfilmu WHERE postavy.schvaleno = 1 ORDER BY postavy.datumPridani DESC;";
            ResultSet resultSet = statement.executeQuery(recentlyAddedQuery);
            int countRecentlyAdded = 0;
            while(resultSet.next()){
                if(countRecentlyAdded < 6){
                   Character character = new Character();
                   character.setId(resultSet.getInt("id"));
                   character.setName(resultSet.getString("jmeno"));
                    byte[] imageBytes = resultSet.getBytes("obrazek");
                    if (imageBytes != null) {
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        character.setImage(base64Image); // Nastav Base64 obrázek
                    }                    character.setFilmName(resultSet.getString("nazevfilmu"));
                    characters.add(character);
                }
                countRecentlyAdded++;
        }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        */
        return characters;

    }
    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT c.idCharacter, c.name, c.picture, c.type, c.gender, c.dateAdded, u.username AS addedBy, admin.username AS approvedBy, actor.name AS actorName, dabber.name AS dabberName, GROUP_CONCAT(m.nameMovie SEPARATOR ', ') AS movies FROM characters c LEFT JOIN users u ON c.addedBy = u.idUser LEFT JOIN users admin ON c.approvedBy = admin.idUser LEFT JOIN actors actor ON c.idActor = actor.idActor LEFT JOIN actors dabber ON c.idDabber = dabber.idActor LEFT JOIN characters_movies cm ON c.idCharacter = cm.idCharacter LEFT JOIN movies m ON cm.idMovie = m.idMovie WHERE c.approved = 1 GROUP BY c.idCharacter, c.name, c.picture, c.type, c.gender, c.dateAdded, u.username, admin.username, actor.name, dabber.name;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Character character = new Character();
                character.setId(resultSet.getInt("idCharacter"));
                character.setName(resultSet.getString("name"));
                byte[] imageBytes = resultSet.getBytes("picture");
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    character.setImage(base64Image); // Nastav Base64 obrázek
                }
                character.setType(resultSet.getString("type"));
                character.setGender(resultSet.getString("gender"));
                character.setDateAdded(resultSet.getDate("dateAdded"));
                character.setAddedBy(resultSet.getString("addedBy"));
                character.setApprovedBy(resultSet.getString("approvedBy"));

                String actorName = resultSet.getString("actorName");
                if(actorName != null){
                    character.setActorName(actorName);
                }
                String dabberName = resultSet.getString("dabberName");
                if(dabberName != null){
                    character.setDabberName(dabberName);
                }
                String moviesStr = resultSet.getString("movies");
                List<String> movieList = new ArrayList<>();
                if (moviesStr != null && !moviesStr.isEmpty()) {
                    // Rozdělíme řetězec podle čárky a případně odstraníme mezery
                    movieList = new ArrayList<>(Arrays.asList(moviesStr.split(",\\s*")));
                }
                character.setMovieList(movieList);


                characters.add(character);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching characters", e);
        }

        return characters;
    }

    // Načtení celkového počtu postav (pro stránkování)
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM postavy";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching total count", e);
        }

        return 0;
    }

    //method to approve character (to start displaying it on web)
    public void approveCharacter(int id, int userId){
        try {
            String query = "UPDATE characters SET approved = 1, approvedBy = ?  WHERE idCharacter = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //method to NOT approve character - delete it from database
    public void deleteCharacter(int id){
        List<Integer> actorIds = new ArrayList<>();
        List<Integer> movieIds = new ArrayList<>();
        try {
            //select to get id of actor and id of film
            String selectQuery = "SELECT idActor, idDabber FROM characters WHERE idCharacter = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            while(resultSet.next()){
                actorIds.add(resultSet.getInt("idActor"));
                actorIds.add(resultSet.getInt("idDabber"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String selectQuery = "SELECT idMovie FROM characters_movies WHERE idCharacter = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            while(resultSet.next()){
                movieIds.add(resultSet.getInt("idMovie"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            String query = "DELETE FROM characters WHERE idCharacter = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            //deleting the character's quotes as well
            QuotesDAO quotesDAO = new QuotesDAO();
            quotesDAO.deleteQuotes(id);
            //deleting character
            statement.executeUpdate();
            //deleting the actor if doesnt have any other characters
            ActorDAO actorDAO = new ActorDAO();
            actorDAO.deleteActorIfNotUsed(actorIds.get(0));
            actorDAO.deleteActorIfNotUsed(actorIds.get(1));
            //deleting the film if doesnt have any other characters
            FilmDAO filmDAO = new FilmDAO();
            for(int i = 0; i < movieIds.size(); i++) {
                filmDAO.deleteFilmIfNotUsed(movieIds.get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Character> getUnapprovedCharacters(){
        List<Character> characters = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            //getting characters from the database for the "recently added" section
            String sql = "SELECT c.idCharacter, c.description, c.name, c.picture, c.type, c.gender, c.dateAdded, u.username AS addedBy, admin.username AS approvedBy, actor.name AS actorName, dabber.name AS dabberName, GROUP_CONCAT(m.nameMovie SEPARATOR ', ') AS movies FROM characters c LEFT JOIN users u ON c.addedBy = u.idUser LEFT JOIN users admin ON c.approvedBy = admin.idUser LEFT JOIN actors actor ON c.idActor = actor.idActor LEFT JOIN actors dabber ON c.idDabber = dabber.idActor LEFT JOIN characters_movies cm ON c.idCharacter = cm.idCharacter LEFT JOIN movies m ON cm.idMovie = m.idMovie WHERE c.approved = 0 GROUP BY c.idCharacter, c.name, c.picture, c.type, c.gender, c.dateAdded, u.username, admin.username, actor.name, dabber.name;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Character character = new Character();
                character.setId(resultSet.getInt("idCharacter"));
                character.setName(resultSet.getString("name"));
                character.setDesc(resultSet.getString("description"));
                byte[] imageBytes = resultSet.getBytes("picture");
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    character.setImage(base64Image); // Nastav Base64 obrázek
                }
                character.setType(resultSet.getString("type"));
                character.setGender(resultSet.getString("gender"));
                character.setDateAdded(resultSet.getDate("dateAdded"));
                character.setAddedBy(resultSet.getString("addedBy"));
                character.setApprovedBy(resultSet.getString("approvedBy"));

                String actorName = resultSet.getString("actorName");
                if(actorName != null){
                    character.setActorName(actorName);
                }
                String dabberName = resultSet.getString("dabberName");
                if(dabberName != null){
                    character.setDabberName(dabberName);
                }
                String moviesStr = resultSet.getString("movies");
                List<String> movieList = new ArrayList<>();
                if (moviesStr != null && !moviesStr.isEmpty()) {
                    // Rozdělíme řetězec podle čárky a případně odstraníme mezery
                    movieList = new ArrayList<>(Arrays.asList(moviesStr.split(",\\s*")));
                }
                character.setMovieList(movieList);

                characters.add(character);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characters;

    }
}
