package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import model.Pemasukan;
import model.Pengeluaran;
import model.Transaksi;
import model.User;

public class InitDatabase {

    public static void init() {
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL
            );
        """;

        String sqlSettings = """
            CREATE TABLE IF NOT EXISTS settings (
                user_id INTEGER PRIMARY KEY,
                daily_limit REAL DEFAULT 0
            );
        """;

        String sqlTransaksi = """
            CREATE TABLE IF NOT EXISTS transaksi (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                jenis TEXT NOT NULL,
                nominal REAL NOT NULL,
                tanggal TEXT NOT NULL,
                catatan TEXT
            );
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsers);
            stmt.execute(sqlSettings);
            stmt.execute(sqlTransaksi);

            if (getRecordCount("users") == 0) {
                System.out.println("Membuat user permanen...");
                User mainUser = new User("mahasiswa", "123");
                UserDAO.registerUser(mainUser);

                SettingsDAO.saveDailyLimit(1, 50000); 
            }

            if (getRecordCount("transaksi") == 0) {
                System.out.println("Mengisi data sejarah transaksi...");
                
                int userId = 1;

                insert(new Pemasukan(userId, 3500000, LocalDate.now().minusDays(7), "Kiriman Bulanan Ortu"));
                insert(new Pengeluaran(userId, 15000, LocalDate.now().minusDays(7), "Makan Siang Warteg"));

                insert(new Pengeluaran(userId, 25000, LocalDate.now().minusDays(5), "Beli Pulsa Paket Data"));
                insert(new Pengeluaran(userId, 12000, LocalDate.now().minusDays(5), "Pentol Teknik"));

                insert(new Pengeluaran(userId, 850000, LocalDate.now().minusDays(3), "Bayar Kos-Kosan"));
                insert(new Pemasukan(userId, 50000, LocalDate.now().minusDays(3), "Jual Hp Bekas"));

                insert(new Pengeluaran(userId, 12000, LocalDate.now().minusDays(1), "Nasi Goreng Depan Glow"));
                insert(new Pengeluaran(userId, 3000, LocalDate.now().minusDays(1), "Parkir Kopi Kenangan"));

                insert(new Pengeluaran(userId, 15000, LocalDate.now(), "Sarapan Bu Darmi"));
                insert(new Pengeluaran(userId, 10000, LocalDate.now(), "Pentol Teknik"));

                insert(new Pengeluaran(userId, 24000, LocalDate.now(), "Bensin Motor"));
            }

            System.out.println("Database siap digunakan.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insert(Transaksi t) {
        TransaksiDAO.insert(t);
    }

    private static int getRecordCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}