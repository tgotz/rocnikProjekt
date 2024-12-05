package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.List;
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
    public void updateCharacter(Character character, User user){
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
    }
    //method to add character
    public void addCharacter(Character character, String[] quotesArray){
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
        //building sql query based on what "values" were provided- nickname and age are optional.
        String sqlPart1 = "INSERT INTO postavy (jmeno, popis, datumPridani, typPostavy, pohlavi, obrazek, idherce, iduser, idfilmu";
        String values = "(?, ?, NOW(), ?, ?, ?, ?, ?, ?";

        //checking if age != default value (999)
        if(character.getAge() != 999){
            sqlPart1 += ", vek";
            values += ", ?";
        }
        //checking if nickname was provided
        if(character.getNickname() != null){
            sqlPart1 += ", prezdivka";
            values += ", ?";
        }
        //finishing the query
        sqlPart1 += ")";
        values += ")";
        String query = sqlPart1 + " VALUES " + values;
        try {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1,character.getName());
            statement.setString(2,character.getDesc());
            statement.setString(3,character.getType());
            statement.setString(4,character.getGender());
            statement.setBytes(5,character.getImageBytes());
            statement.setInt(6,actorId);
            statement.setInt(7,1);
            statement.setInt(8,(filmId));
            if(character.getAge() != 999){
                statement.setInt(9, character.getAge());
            }
            if(character.getNickname() != null){
                if(character.getAge() ==999){
                    statement.setString(9, character.getNickname());
                } else{
                    statement.setString(10, character.getNickname());

                }
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    //method to get character's details - for detail jsp
    public Character getCharacterDetail(int id){
        String query = "SELECT * FROM postavy JOIN filmy on postavy.idfilmu = filmy.idfilmu JOIN herci on postavy.idherce = herci.idherce JOIN users on postavy.iduser = users.iduser WHERE postavy.idpostavy = ?";
        Character character = new Character();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                character.setId(resultSet.getInt("postavy.idpostavy"));
                character.setName(resultSet.getString("postavy.jmeno"));
                character.setDesc(resultSet.getString("postavy.popis"));
                character.setDateAdded(resultSet.getDate("postavy.datumPridani"));
                character.setType(resultSet.getString("postavy.typPostavy"));
                character.setGender(resultSet.getString("postavy.pohlavi"));
                byte[] imageBytes = resultSet.getBytes("obrazek");
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    character.setImage(base64Image); // Nastav Base64 obrázek
                }                character.setNickname(resultSet.getString("postavy.prezdivka"));
                character.setActorName(resultSet.getString("herci.jmeno"));
                character.setAdminName(resultSet.getString("users.username"));
                character.setFilmName(resultSet.getString("filmy.nazevFilmu"));
                character.setAge(resultSet.getInt("postavy.vek"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return character;
    };
    //method to get characters for Leaderboards
    public ArrayList<Character> getCharactersLB(String orderBy){
        String query = "SELECT postavy.idpostavy , postavy.jmeno, postavy.prezdivka, filmy.nazevFilmu, herci.jmeno, AVG(recenze.celkoveHodnoceni) as overall, AVG(recenze.hodnoceniAtraktivity) as attractiveness FROM postavy JOIN filmy on postavy.idfilmu = filmy.idfilmu JOIN herci on postavy.idherce = herci.idherce JOIN recenze on postavy.idpostavy = recenze.idpostavy GROUP BY (postavy.idpostavy) ORDER BY " + orderBy;
        ArrayList<Character> characterList = new ArrayList<Character>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()){
                if(i < 50){
                    Character character = new Character();
                    character.setId(resultSet.getInt("postavy.idpostavy"));
                    character.setName(resultSet.getString("postavy.jmeno"));
                    character.setNickname(resultSet.getString("postavy.prezdivka"));
                    character.setFilmName(resultSet.getString("filmy.nazevFilmu"));
                    character.setActorName(resultSet.getString("herci.jmeno"));
                    character.setOverallRating(resultSet.getDouble("overall"));
                    character.setAttractivenessRating(resultSet.getDouble("attractiveness"));
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
        ArrayList<Character> characters = new ArrayList<Character>();
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
        return characters;

    }
    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT postavy.idpostavy as id, postavy.jmeno as jmeno, postavy.obrazek as obrazek, " +
                "postavy.typPostavy as typ, postavy.pohlavi as pohlavi, filmy.nazevFilmu as nazevFilmu, postavy.datumPridani as datumPridani, herci.jmeno as herec, users.username as username  " +
                "FROM postavy " +
                "JOIN filmy ON filmy.idfilmu = postavy.idfilmu " +
                "JOIN herci ON herci.idherce = postavy.idherce " +
                "JOIN users ON users.iduser = postavy.iduser " +
                "WHERE postavy.schvaleno = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Character character = new Character();
                character.setId(resultSet.getInt("id"));
                character.setName(resultSet.getString("jmeno"));
                byte[] imageBytes = resultSet.getBytes("obrazek");
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    character.setImage(base64Image); // Nastav Base64 obrázek
                }
                character.setType(resultSet.getString("typ"));
                character.setGender(resultSet.getString("pohlavi"));
                character.setFilmName(resultSet.getString("nazevFilmu"));
                character.setDateAdded(resultSet.getDate("datumPridani"));
                character.setAdminName(resultSet.getString("username"));
                character.setActorName(resultSet.getString("herec"));
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
            String query = "UPDATE postavy SET schvaleno = 1, iduser = ?  WHERE idpostavy = ?";
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
        int idActor = 0;
        int idFilm = 0;
        try {
            //select to get id of actor and id of film
            String selectQuery = "SELECT idherce, idfilmu FROM postavy WHERE idpostavy = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            while(resultSet.next()){
                idActor = resultSet.getInt("idherce");
                idFilm = resultSet.getInt("idfilmu");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String query = "DELETE FROM postavy WHERE idpostavy = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            //deleting the character's quotes as well
            QuotesDAO quotesDAO = new QuotesDAO();
            quotesDAO.deleteQuotes(id);
            //deleting character
            statement.executeUpdate();
            //deleting the actor if doesnt have any other characters
            ActorDAO actorDAO = new ActorDAO();
            actorDAO.deleteActorIfNotUsed(idActor);
            //deleting the film if doesnt have any other characters
            FilmDAO filmDAO = new FilmDAO();
            filmDAO.deleteFilmIfNotUsed(idFilm);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Character> getUnapprovedCharacters(){
        List<Character> characters = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            //getting characters from the database for the "recently added" section
            String unapprovedCharactersQuery = "SELECT postavy.idpostavy as id, postavy.jmeno as jmeno, postavy.obrazek as obrazek, postavy.typPostavy as typ, postavy.pohlavi as pohlavi, filmy.nazevFilmu as nazevFilmu, postavy.datumPridani as datumPridani, postavy.popis as popis, herci.jmeno as herec, postavy.prezdivka as prezdivka FROM postavy JOIN filmy ON filmy.idfilmu = postavy.idfilmu JOIN herci ON herci.idherce = postavy.idherce WHERE postavy.schvaleno = 0;\n";
            ResultSet resultSet = statement.executeQuery(unapprovedCharactersQuery);
            while(resultSet.next()){
                    Character character = new Character();
                    character.setId(resultSet.getInt("id"));
                    character.setName(resultSet.getString("jmeno"));
                    byte[] imageBytes = resultSet.getBytes("obrazek");
                    if (imageBytes != null) {
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        character.setImage(base64Image); // Nastav Base64 obrázek
                    }
                    character.setFilmName(resultSet.getString("nazevfilmu"));
                    character.setNickname(resultSet.getString("prezdivka"));
                    character.setDesc(resultSet.getString("popis"));
                    character.setActorName(resultSet.getString("herec"));
                    character.setType(resultSet.getString("typ"));
                    character.setGender(resultSet.getString("pohlavi"));
                    characters.add(character);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characters;

    }
}
