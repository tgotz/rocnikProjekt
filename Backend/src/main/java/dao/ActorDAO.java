package dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class ActorDAO {
    private Connection connection;

    public ActorDAO() {
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

    //Checks if actor exists
    public int getActorId(String actorName) {
        String query = "SELECT idActor FROM actors WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, actorName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idActor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //inserting actor (in case he doesnt exists)
    public int insertActor(String actorName) {
        System.out.println("Zkouším vkládat ");
        String query = "INSERT INTO actors (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, actorName);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    //delete actor if he doesn't have any characters
    public void deleteActorIfNotUsed(int actorId) {
        try {
            String query = "DELETE FROM actors WHERE idActor = ? AND NOT EXISTS (SELECT 1 FROM characters WHERE idActor = ?)";
            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, actorId);
            statement.setInt(2, actorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}