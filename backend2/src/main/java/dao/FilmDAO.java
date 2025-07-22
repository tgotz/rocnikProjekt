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
        String query = "SELECT nameMovie FROM movies WHERE nameMovie LIKE ?";
        try (
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + input + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmNames.add(resultSet.getString("nameMovie"));
                }
            }
        }
        return filmNames;
    }
    public int getFilmId(String filmName) {
        String query = "SELECT idMovie FROM movies WHERE nameMovie = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, filmName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idMovie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int insertFilm(String filmName) {
        System.out.println("zkousim vkladat ");
        String query = "INSERT INTO movies (nameMovie) VALUES (?)";
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
            String query = "DELETE FROM movies WHERE idMovie = ? AND NOT EXISTS (SELECT 1 FROM characters_movies where idMovie = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, filmId);
            statement.setInt(2, filmId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void assignFilm(int idMovie, int idCharacter) {
        String query = "INSERT INTO characters_movies (idCharacter, idMovie) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idCharacter);
            statement.setInt(2, idMovie);
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při přiřazování filmu k postavě: " + e.getMessage(), e);
        }
    }
    public void deleteAssignedFilms(int idCharacter){
        try {
            String query = "DELETE FROM characters_movies WHERE idCharacter = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idCharacter);
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
