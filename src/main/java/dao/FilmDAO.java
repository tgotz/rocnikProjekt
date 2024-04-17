package dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FilmDAO {
    // Database connection details
    private Connection connection;
    public FilmDAO() {
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
    // Method to fetch film names based on user input
    public List<String> findFilmNames(String input) throws SQLException {
        List<String> filmNames = new ArrayList<>();
        String query = "SELECT nazevFilmu FROM filmy WHERE nazevFilmu LIKE ?";
        try (
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
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
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
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
    public void deleteFilmIfNotUsed(int filmId) {
        try {
            String query = "DELETE FROM filmy WHERE idfilmu = ? AND NOT EXISTS (SELECT 1 FROM postavy where idfilmu = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, filmId);
            statement.setInt(2, filmId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
