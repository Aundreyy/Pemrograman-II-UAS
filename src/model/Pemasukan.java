package model;

import java.time.LocalDate;

public class Pemasukan extends Transaksi implements Laporan {

    public Pemasukan(int userId, double nominal, LocalDate tanggal, String catatan) {
        super(userId, nominal, tanggal, catatan);
        this.setJenis("Pemasukan");
    }

    @Override
    public String cetakLaporan() {
        return "[+] DANA MASUK: Rp " + getNominal() + " (" + getCatatan() + ")";
    }
}