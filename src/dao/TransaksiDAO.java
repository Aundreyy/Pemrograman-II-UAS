package dao;

import model.Transaksi;
import model.Pemasukan;
import model.Pengeluaran;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    public static void insert(Transaksi t) {

        String sql =
                "INSERT INTO transaksi (user_id, jenis, nominal, tanggal, catatan) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getJenis());
            ps.setDouble(3, t.getNominal());
            ps.setString(4, t.getTanggal().toString());
            ps.setString(5, t.getCatatan());

            ps.executeUpdate();
            ps.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Gagal insert transaksi", e);
        }
    }

    public static void delete(int transaksiId) {

        String sql = "DELETE FROM transaksi WHERE id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, transaksiId);
            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Gagal hapus transaksi", e);
        }
    }
    // EDIT
    public static void update(Transaksi t) {
        String sql = "UPDATE transaksi SET nominal = ?, catatan = ?, jenis = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, t.getNominal());
            stmt.setString(2, t.getCatatan());

            stmt.setString(3, t.getJenis());
            
            stmt.setInt(4, t.getId());

            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Transaksi> getByUserAndDate(int userId, LocalDate date) {

        List<Transaksi> list = new ArrayList<>();

        String sql =
                "SELECT * FROM transaksi " +
                "WHERE user_id = ? AND tanggal = ? " +
                "ORDER BY id DESC";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, date.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String jenis = rs.getString("jenis");
                Transaksi t;

                if ("Pemasukan".equalsIgnoreCase(jenis)) {
                    t = new Pemasukan(
                            rs.getInt("user_id"),
                            rs.getDouble("nominal"),
                            LocalDate.parse(rs.getString("tanggal")),
                            rs.getString("catatan")
                    );
                } else {
                    t = new Pengeluaran(
                            rs.getInt("user_id"),
                            rs.getDouble("nominal"),
                            LocalDate.parse(rs.getString("tanggal")),
                            rs.getString("catatan")
                    );
                }

                t.setId(rs.getInt("id"));
                list.add(t);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Gagal ambil transaksi", e);
        }

        return list;
    }

    public static double getTotalPengeluaranHarian(int userId, LocalDate date) {

        String sql =
                "SELECT COALESCE(SUM(nominal), 0) FROM transaksi " +
                "WHERE user_id = ? AND tanggal = ? AND jenis = 'Pengeluaran'";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, date.toString());

            ResultSet rs = ps.executeQuery();
            double result = rs.next() ? rs.getDouble(1) : 0;

            rs.close();
            ps.close();
            conn.close();

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Gagal hitung pengeluaran", e);
        }
    }

    public static double getTotalPemasukanHarian(int userId, LocalDate date) {

        String sql =
                "SELECT COALESCE(SUM(nominal), 0) FROM transaksi " +
                "WHERE user_id = ? AND tanggal = ? AND jenis = 'Pemasukan'";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, date.toString());

            ResultSet rs = ps.executeQuery();
            double result = rs.next() ? rs.getDouble(1) : 0;

            rs.close();
            ps.close();
            conn.close();

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Gagal hitung pemasukan", e);
        }
    }

    public static double getRataRataPengeluaranHarianByKeyword(
            int userId,
            String keyword
    ) {

        String sql = """
            SELECT COALESCE(AVG(nominal), 0)
            FROM transaksi
            WHERE user_id = ?
            AND jenis = 'Pengeluaran'
            AND catatan LIKE ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<Transaksi> getAllByUser(int userId) {
        List<Transaksi> list = new ArrayList<>();

        String sql = "SELECT * FROM transaksi WHERE user_id = ? ORDER BY tanggal DESC, id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String jenis = rs.getString("jenis");
                Transaksi t;

                if ("Pemasukan".equalsIgnoreCase(jenis)) {
                    t = new Pemasukan(
                        rs.getInt("user_id"),
                        rs.getDouble("nominal"),
                        LocalDate.parse(rs.getString("tanggal")),
                        rs.getString("catatan")
                    );
                } else {
                    t = new Pengeluaran(
                        rs.getInt("user_id"),
                        rs.getDouble("nominal"),
                        LocalDate.parse(rs.getString("tanggal")),
                        rs.getString("catatan")
                    );
                }
                t.setId(rs.getInt("id"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
