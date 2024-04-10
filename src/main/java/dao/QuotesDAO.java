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
        String sql = "INSERT INTO hlasky (textHlasky, idpostavy) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hlaska);
            statement.setInt(2, idPostavy);
            statement.executeUpdate();
        }
    }

    public ArrayList<String> getHlasky(int id){
        String query = "SELECT texthlasky FROM hlasky WHERE idpostavy = ?";
        ArrayList<String> quotesList = new ArrayList<String>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                quotesList.add(resultSet.getString("textHlasky"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quotesList;
    }
}