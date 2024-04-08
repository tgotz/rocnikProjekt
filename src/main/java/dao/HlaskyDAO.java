package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HlaskyDAO {

    private final Connection connection;

    public HlaskyDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertHlaska(String hlaska, int idPostavy) throws SQLException {
        String sql = "INSERT INTO hlasky (textHlasky, idpostavy) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hlaska);
            statement.setInt(2, idPostavy);
            statement.executeUpdate();
        }
    }
}