package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsDAO {

    public static double getDailyLimit(int userId) {
        String sql = "SELECT daily_limit FROM settings WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("daily_limit");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void saveDailyLimit(int userId, double limit) {
        String updateSql = "UPDATE settings SET daily_limit = ? WHERE user_id = ?";
        String insertSql = "INSERT INTO settings (user_id, daily_limit) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {

            ps.setDouble(1, limit);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                try (PreparedStatement ps2 = conn.prepareStatement(insertSql)) {
                    ps2.setInt(1, userId);
                    ps2.setDouble(2, limit);
                    ps2.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}