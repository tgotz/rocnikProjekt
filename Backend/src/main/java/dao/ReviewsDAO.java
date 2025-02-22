package dao;

import model.Character;
import model.Review;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ReviewsDAO {
    private Connection connection;

    public ReviewsDAO() {
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

    public ReviewsDAO(Connection connection) {}
    public ArrayList<Review> getReviews(int id){
        String query = "SELECT * FROM reviews WHERE idCharacter = ?";
        ArrayList<Review> reviewList = new ArrayList<Review>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Review review = new Review();
                review.setReviewId(resultSet.getInt("idReview"));
                review.setAuthorName(resultSet.getString("nameAuthor"));
                review.setOverallRating(resultSet.getInt("overalRating"));
                review.setAttractivenessRating(resultSet.getInt("attractivenessRating"));
                review.setDateAdded(resultSet.getDate("dateAdded"));
                review.setReviewText(resultSet.getString("textReview"));
                review.setCharacterId(resultSet.getInt("idCharacter"));
                reviewList.add(review);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reviewList;
    }
    public void insertReview(Review review){
        String query = "INSERT INTO reviews (nameAuthor, overalRating, attractivenessRating, dateAdded, textReview, idCharacter) VALUES (?, ?, ?, NOW(), ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, review.getAuthorName());
            statement.setInt(2, review.getOverallRating());
            statement.setInt(3, review.getAttractivenessRating());
            statement.setString(4, review.getReviewText());
            statement.setInt(5, review.getCharacterId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
}
