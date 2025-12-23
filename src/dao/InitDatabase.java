package dao;

import java.sql.Connection;
import java.sql.Statement;
import model.User; // Pastikan ada import ini

public class InitDatabase {

    public static void init() {

        // 1. QUERY PEMBUATAN TABEL (TETAP SAMA)
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

        try (
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            // Eksekusi pembuatan tabel
            stmt.execute(sqlUsers);
            stmt.execute(sqlSettings);
            stmt.execute(sqlTransaksi);
            
            // ============================================================
            // BAGIAN PENTING: BUKTI PENGGUNAAN CONSTRUCTOR KE-2
            // ============================================================
            
            // "Pak, di sini saya menggunakan Constructor User(username, password)
            // untuk membuat objek user default."
            User defaultUser = new User("mahasiswa", "123"); 
            
            // Lalu saya simpan menggunakan DAO
            UserDAO.registerUser(defaultUser);

            System.out.println("Database & User Default siap digunakan.");

        } catch (Exception e) {
            System.err.println("Gagal inisialisasi database!");
            e.printStackTrace();
        }
    }
}