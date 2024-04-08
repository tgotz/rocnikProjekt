package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/characters1";
    private static final String USERNAME = "webik";
    private static final String PASSWORD = "webik69";

    // Method to establish database connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    public FilmDAO(){

    };
    // Method to fetch film names based on user input
    public List<String> findFilmNames(String input) throws SQLException {
        List<String> filmNames = new ArrayList<>();
        String query = "SELECT nazevFilmu FROM filmy WHERE nazevFilmu LIKE ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + input + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmNames.add(resultSet.getString("nazevFilmu"));
                }
            }
        }
        return filmNames;
    }
    public int getFilmId(String filmName) {
        String query = "SELECT idfilmu FROM filmy WHERE nazevFilmu = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, filmName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idfilmu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int insertFilm(String filmName) {
        System.out.println("zkousim vkladat ");
        String query = "INSERT INTO filmy (nazevFilmu) VALUES (?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, filmName);
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
