package service;

import model.Transaksi;

import java.time.LocalDate;
import java.util.List;

import dao.SettingsDAO;
import dao.TransaksiDAO;

public class TransaksiService {
    public static class DailyCheckResult {
        public final String status;
        public final double limit;

        public DailyCheckResult(String status, double limit) {
            this.status = status;
            this.limit = limit;
        }
    }

    public DailyCheckResult tambahTransaksi(Transaksi t) {
        TransaksiDAO.insert(t);

        return cekStatusHarian(t.getUserId());
    }

    public void hapusTransaksi(int transaksiId) {
        TransaksiDAO.delete(transaksiId);
    }
    
    public void updateTransaksi(Transaksi t) {
        TransaksiDAO.update(t);
    }

    public List<Transaksi> getTransaksiHarian(int userId, LocalDate tanggal) {
        return TransaksiDAO.getByUserAndDate(userId, tanggal);
    }

    public double getTotalPengeluaranHarian(int userId, LocalDate tanggal) {
        return TransaksiDAO.getTotalPengeluaranHarian(userId, tanggal);
    }

    public double getTotalPemasukanHarian(int userId, LocalDate tanggal) {
        return TransaksiDAO.getTotalPemasukanHarian(userId, tanggal);
    }

    public DailyCheckResult cekStatusHarian(int userId) {

        double totalPengeluaran =
                TransaksiDAO.getTotalPengeluaranHarian(
                        userId, LocalDate.now()
                );

        double limit = SettingsDAO.getDailyLimit(userId);

        String status =
                totalPengeluaran > limit ? "WASPADA" : "AMAN";

        return new DailyCheckResult(status, limit);
    }

    public double simulasiRataRataPengeluaran(
            int userId,
            String keyword
    ) {
        return TransaksiDAO
                .getRataRataPengeluaranHarianByKeyword(
                        userId,
                        keyword
                );
    }

    public void updateDailyLimit(int userId, double limit) {
        SettingsDAO.saveDailyLimit(userId, limit);
    }

    public List<Transaksi> getAllTransaksi(int userId) {
        return TransaksiDAO.getAllByUser(userId);
    }
}