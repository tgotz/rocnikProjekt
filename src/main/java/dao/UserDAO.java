package dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class UserDAO {
    private Connection connection;

    public UserDAO() {

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

    public User getUseByUsername(String username) {
        User user = new User();
        String query = "SELECT * FROM administratori WHERE username = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("idadministrator"));
                user.setName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
    public boolean verifyPassword(String username, String password){
        User user = getUseByUsername(username);
        if(user.getPassword() != null){
            return BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified;
        }
        return false;
    }

}
