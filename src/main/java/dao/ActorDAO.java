package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActorDAO {
    private Connection connection;

    public ActorDAO(Connection connection) {
        this.connection = connection;
    }

    //Checks if actor exists
    public int getActorId(String actorName) {
        String query = "SELECT idherce FROM herci WHERE jmeno = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, actorName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idherce");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //inserting actor (in case he doesnt exists)
    public int insertActor(String actorName) {
        System.out.println("zkousim vkladat ");
        String query = "INSERT INTO herci (jmeno) VALUES (?)";
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
}