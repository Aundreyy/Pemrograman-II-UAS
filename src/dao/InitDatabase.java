package dao;

import java.sql.Connection;
import java.sql.Statement;

public class InitDatabase {

    public static void init() {

        // =========================
        // TABEL USERS (LOGIN)
        // =========================
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL
            );
        """;

        // =========================
        // TABEL SETTINGS (BATAS HARIAN)
        // =========================
        String sqlSettings = """
            CREATE TABLE IF NOT EXISTS settings (
                user_id INTEGER PRIMARY KEY,
                daily_limit REAL DEFAULT 0
            );
        """;

        // =========================
        // TABEL TRANSAKSI
        // =========================
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

        // =========================
        // USER DEFAULT (UNTUK LOGIN)
        // =========================
        String sqlInsertDefaultUser = """
            INSERT OR IGNORE INTO users (id, username, password_hash)
            VALUES (1, 'mahasiswa', '123');
        """;

        try (
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            // Buat tabel
            stmt.execute(sqlUsers);
            stmt.execute(sqlSettings);
            stmt.execute(sqlTransaksi);

            // Insert user default
            stmt.execute(sqlInsertDefaultUser);

            System.out.println("Database & tabel berhasil diinisialisasi.");

        } catch (Exception e) {
            System.err.println("Gagal inisialisasi database!");
            e.printStackTrace();
        }
    }
}