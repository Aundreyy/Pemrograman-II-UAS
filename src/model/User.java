package model;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private double dailyLimit;

    // =========================
    // Constructor untuk LOGIN
    // =========================
    public User(int id, String username, String passwordHash, double dailyLimit) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.dailyLimit = dailyLimit;
    }

    // =========================
    // GETTER (Encapsulation)
    // =========================
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    // =========================
    // SETTER (khusus yang perlu)
    // =========================
    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}
