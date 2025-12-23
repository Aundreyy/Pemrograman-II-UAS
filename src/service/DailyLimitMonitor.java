package service;

public class DailyLimitMonitor {


    public boolean isOverLimit(double totalHarian, double limit) {
        if (limit <= 0) return false;
        return totalHarian > limit;
    }

    public String getStatusHarian(double totalHarian, double limit) {
        if (limit <= 0) return "BELUM DISET";
        return isOverLimit(totalHarian, limit) ? "MELEBIHI" : "AMAN";
    }

    public double getProgress(double totalHarian, double limit) {
        if (limit <= 0) return 0;
        return Math.min(1.0, totalHarian / limit);
    }
}