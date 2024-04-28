package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;

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
        String query = "UPDATE postavy SET jmeno = ?, popis = ?, datumPridani = NOW(), typPostavy = ?, pohlavi = ?, idherce = ?, idadministrator = ?, idfilmu = ?";

        //checking if age != default value (999)
        if(character.getAge() != 999){
            query += ", vek = ?";

        }
        //checking if nickname was provided
        if(character.getNickname() != null){
            query += ", prezdivka = ?";
        }
        //checking if Image was provided
        if(character.getImage() != null){
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
            if(character.getImage() != null){

                statement.setBytes(i, character.getImage());
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
        String sqlPart1 = "INSERT INTO postavy (jmeno, popis, datumPridani, typPostavy, pohlavi, obrazek, idherce, idadministrator, idfilmu";
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
            statement.setBytes(5,character.getImage());
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
        String query = "SELECT * FROM postavy JOIN filmy on postavy.idfilmu = filmy.idfilmu JOIN herci on postavy.idherce = herci.idherce JOIN administratori on postavy.idadministrator = administratori.idadministrator WHERE postavy.idpostavy = ?";
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
                character.setImage(resultSet.getBytes("postavy.obrazek"));
                character.setNickname(resultSet.getString("postavy.prezdivka"));
                character.setActorName(resultSet.getString("herci.jmeno"));
                character.setAdminName(resultSet.getString("administratori.idadministrator"));
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
                    character.setImage(resultSet.getBytes("obrazek"));
                    character.setFilmName(resultSet.getString("nazevfilmu"));
                    characters.add(character);
                }
                countRecentlyAdded++;
        }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characters;

    }
    //method to get characters (including filters etc)
    public ArrayList<Character> getCharacters(HttpServletRequest request){

        ArrayList<Character> characters = new ArrayList<>();
        String sql = "SELECT postavy.idpostavy as id, postavy.popis as popis, postavy.vek as age, postavy.typPostavy as typ,postavy.prezdivka as nickname , postavy.pohlavi as pohlavi, postavy.jmeno as jmeno, postavy.obrazek as obrazek, postavy.schvaleno as schvaleno, postavy.datumPridani as datum, filmy.nazevFilmu as nazevFilmu, herci.jmeno as jmenoHerce, administratori.username as admin FROM postavy JOIN filmy ON filmy.idfilmu = postavy.idfilmu JOIN herci on herci.idherce = postavy.idherce JOIN administratori ON postavy.idadministrator = administratori.idadministrator ";
        ArrayList<String> sqlWhere = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<>();

        //checking if I want approved characters (for users) or unapproved characters (for admins to approve)
       if((Boolean) request.getAttribute("unapproved") != null && (Boolean) request.getAttribute("unapproved")){
            sqlWhere.add("schvaleno = 0");
        } else{
            sqlWhere.add("schvaleno = 1 ");

       }


        //adding search option
        if(request.getParameter("search") != null && !request.getParameter("search").isEmpty() ){
            String search =request.getParameter("search");
            sqlWhere.add("(postavy.jmeno like '%"+ search +"%' OR nazevFilmu like '%" + search +"%')");
        }

        //adding select type option
        if( (request.getParameter("cartoon") == null) ^ request.getParameter("irl") == null){
            if(request.getParameter("cartoon") == null){
                sqlWhere.add(" typPostavy = ?");
                values.add("Hraná");
            } else{
                sqlWhere.add(" typPostavy = ?");
                values.add("Animovaná");

            }
        }

        //adding gender option
        //checks if at least one gender parameter is not selected and at least one is selected.
        if ((request.getParameter("male") == null || request.getParameter("female") == null || request.getParameter("other") == null) && (request.getParameter("male") != null || request.getParameter("female") != null || request.getParameter("other") != null)){
            String genderSqlWhere ="";
            if(request.getParameter("male") != null ){
                genderSqlWhere = "(pohlavi = ? ";
                values.add("Muž");

            }
            if(request.getParameter("female") != null ){
                values.add("Žena");
                if(genderSqlWhere == ""){
                    genderSqlWhere = "(pohlavi = ? ";
                } else{
                    genderSqlWhere += "OR pohlavi = ? ";
                }
            }
            if(request.getParameter("other") != null ){
                if(genderSqlWhere == ""){
                    genderSqlWhere = "(pohlavi = ? ";
                } else{
                    genderSqlWhere += "OR pohlavi = ? ";
                }
                values.add("Jiné");
            }
            genderSqlWhere += ")";
            sqlWhere.add(genderSqlWhere);
        }

        //adding all the where condition to the sql
        if(!sqlWhere.isEmpty()){
            sql += " WHERE ";
            for(int i = 0; i < sqlWhere.size(); i++){
                if( i >= 1){
                    sql += " AND ";
                }
                sql += sqlWhere.get(i);
            }
        }

        //adding order by option
        if(request.getParameter("order") != null && !request.getParameter("order").isEmpty()){
            sql += " ORDER BY " + request.getParameter("order");
        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            //setting the values for sql query
            for(int i = 1; i <= values.size(); i++){
                statement.setString(i, values.get(i-1));
            }
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Character character = new Character();
                character.setId(resultSet.getInt("id"));
                character.setName(resultSet.getString("jmeno"));
                character.setImage(resultSet.getBytes("obrazek"));
                character.setDesc(resultSet.getString("pohlavi"));
                character.setDateAdded(resultSet.getDate("datum"));
                character.setType(resultSet.getString("typ"));
                character.setGender(resultSet.getString("pohlavi"));
                character.setFilmName(resultSet.getString("nazevFilmu"));
                character.setActorName(resultSet.getString("jmenoHerce"));
                character.setNickname(resultSet.getString("nickname"));
                character.setDesc(resultSet.getString("popis"));
                character.setAge(resultSet.getInt("age"));
                character.setAdminName(resultSet.getString("admin"));
                characters.add(character);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return characters;
    }
    //method to approve character (to start displaying it on web)
    public void approveCharacter(int id, User user){
        try {
            String query = "UPDATE postavy SET schvaleno = 1, idadministrator = ?  WHERE idpostavy = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
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
}
