package dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class QuotesDAO {

    private Connection connection;

    public QuotesDAO() {
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
    public void insertQuote(String hlaska, int idPostavy) throws SQLException {
        String sql = "INSERT INTO quotes (textQuote, idCharacter) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hlaska);
            statement.setInt(2, idPostavy);
            statement.executeUpdate();
        }
    }

    public ArrayList<String> getQuotes(int id){
        String query = "SELECT textQuote FROM quotes WHERE idCharacter = ?";
        ArrayList<String> quotesList = new ArrayList<String>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                quotesList.add(resultSet.getString("textQuote"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quotesList;
    }
    public void deleteQuotes(int idPostavy){
        try {
            String query = "DELETE FROM quotes WHERE idCharacter = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idPostavy);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}