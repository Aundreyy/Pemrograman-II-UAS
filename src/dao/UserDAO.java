package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public static User findByUsername(String username) {

        String sql = """
            SELECT u.id, u.username, u.password_hash,
                   IFNULL(s.daily_limit, 0) AS daily_limit
            FROM users u
            LEFT JOIN settings s ON u.id = s.user_id
            WHERE u.username = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getDouble("daily_limit")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void registerUser(User user) {
        String sql = "INSERT OR IGNORE INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());

            ps.executeUpdate();

            User savedUser = findByUsername(user.getUsername());
            if (savedUser != null) {
                SettingsDAO.saveDailyLimit(savedUser.getId(), 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}