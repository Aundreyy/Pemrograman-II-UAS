package model;

import java.time.LocalDate;

// Tambahkan "implements Laporan"
public class Pemasukan extends Transaksi implements Laporan {

    public Pemasukan(int userId, double nominal, LocalDate tanggal, String catatan) {
        super(userId, nominal, tanggal, catatan);
        this.setJenis("Pemasukan");
    }

    // Override method dari Interface
    @Override
    public String cetakLaporan() {
        return "[+] DANA MASUK: Rp " + getNominal() + " (" + getCatatan() + ")";
    }
}