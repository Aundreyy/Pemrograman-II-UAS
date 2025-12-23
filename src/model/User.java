package model;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private double dailyLimit;

    public User(int id, String username, String passwordHash, double dailyLimit) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.dailyLimit = dailyLimit;
    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.dailyLimit = 0;
    }

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

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}