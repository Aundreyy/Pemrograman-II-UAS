package model;

import java.time.LocalDate;

public class Pengeluaran extends Transaksi implements Laporan {

    public Pengeluaran(int userId, double nominal, LocalDate tanggal, String catatan) {
        super(userId, nominal, tanggal, catatan);
        this.setJenis("Pengeluaran");
    }

    @Override
    public String cetakLaporan() {
        return "[-] DANA KELUAR: Rp " + getNominal() + " (" + getCatatan() + ")";
    }
}