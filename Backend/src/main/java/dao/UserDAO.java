package dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Character;
import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
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

    public User getUserByUsername(String username) {
        User user = new User();
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("idUser"));
                user.setName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getInt("idRole"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    public boolean verifyPassword(String username, String password){
        User user = getUserByUsername(username);
        if(user.getPassword() != null){
            return BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified;
        }
        return false;
    }
    public void deleteUser(Integer userid){
        try {
            String query = "DELETE FROM users WHERE idUser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateUserRole(Integer userid, Integer role){
        try {
            System.out.println("Strikam");
            String query = "UPDATE users set idRole = ? WHERE iduser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, role);
            statement.setInt(2, userid);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ajajaa");
            throw new RuntimeException(e);
        }
    }
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("idUser"));
                user.setName(resultSet.getString("username"));
                user.setRole(resultSet.getInt("idRole"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching characters", e);
        }

        return users;
    }
}
