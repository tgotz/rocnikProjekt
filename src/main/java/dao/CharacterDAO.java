package dao;

import java.sql.*;
import java.time.LocalDateTime;

import dao.ActorDAO;
import model.Character;

public class CharacterDAO {
    //change if database details change
    private static final String URL = "jdbc:mysql://localhost:3306/characters1";
    private static final String USERNAME = "webik";
    private static final String PASSWORD = "webik69";
    private Connection connection;
    //database connection
    public CharacterDAO() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public void addCharacter(Character character, String[] quotesArray){
        ActorDAO actorDAO = new ActorDAO(connection);
        FilmDAO filmDAO = new FilmDAO();


        //checking if actor and film already exists
        //if yes - gettings their ID
        //if not - inserting them and getting their ID
        int actorId = actorDAO.getActorId(character.getActorName());
        if(actorId == -1){
            actorId = actorDAO.insertActor(character.getActorName());
        }
        System.out.println(actorId);

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
        System.out.println(query);
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
            HlaskyDAO hlaskyDAO = new HlaskyDAO(connection);
            if(idPostavy != -1){
                for(int i = 0; i < quotesArray.length; i++){
                    if(quotesArray[i] != null && !quotesArray[i].isEmpty()){
                        hlaskyDAO.insertHlaska(quotesArray[i], idPostavy);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

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
                character.setActorName(resultSet.getString("herci.jmeno"));
                character.setAdminName(resultSet.getString("administratori.idadministrator"));
                character.setFilmName(resultSet.getString("filmy.nazevFilmu"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.print(character.getName());
        System.out.print(character.getDesc());
        System.out.print(character.getFilmName());
        return character;
    };
}
