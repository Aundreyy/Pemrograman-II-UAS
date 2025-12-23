package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Pemasukan;
import model.Pengeluaran;
import model.Transaksi;
import service.TransaksiService;

import java.time.LocalDate;

public class TransaksiController {

    @FXML private TextField txtNominal;
    @FXML private TextField txtCatatan;

    private int currentUserId;
    private DashboardController dashboardController;
    private Transaksi transaksiEdit;

    private final TransaksiService transaksiService = new TransaksiService();

    public void setUser(int userId) {
        this.currentUserId = userId;
    }

    public void setDashboardController(DashboardController dc) { 
        this.dashboardController = dc;
    }

    public void setTransaksiEdit(Transaksi t) {
        this.transaksiEdit = t;
        txtNominal.setText(String.valueOf(t.getNominal()));
        txtCatatan.setText(t.getCatatan());
    }

    // ================== HANDLERS ==================
    
    @FXML
    public void handleTambahPemasukan() {
        simpan(true); // true = Pemasukan
    }

    @FXML
    public void handleTambahPengeluaran() {
        simpan(false); // false = Pengeluaran
    }

    // ================== LOGIKA UTAMA ==================
    private void simpan(boolean isPemasukan) {
        try {
            double nominal = Double.parseDouble(txtNominal.getText());
            String catatan = txtCatatan.getText();

            if (transaksiEdit != null) {
                // ================= MODE EDIT (RE-CREATE OBJECT) =================
                // Kita harus membuat objek baru supaya Class-nya berubah (Pemasukan <-> Pengeluaran)
                
                // 1. Ambil ID dan Tanggal dari data lama (biar nggak hilang)
                int idLama = transaksiEdit.getId();
                LocalDate tglLama = transaksiEdit.getTanggal();

                // 2. Buat Objek Baru sesuai tombol yang diklik
                Transaksi tBaru;
                if (isPemasukan) {
                    tBaru = new Pemasukan(currentUserId, nominal, tglLama, catatan);
                } else {
                    tBaru = new Pengeluaran(currentUserId, nominal, tglLama, catatan);
                }

                // 3. PENTING: Paksa ID objek baru sama dengan ID lama
                // Ini kuncinya supaya Database melakukan UPDATE, bukan INSERT
                tBaru.setId(idLama);

                // 4. Kirim ke Service
                transaksiService.updateTransaksi(tBaru);

            } else {
                // ================= MODE BARU (INSERT) =================
                Transaksi t = isPemasukan
                        ? new Pemasukan(currentUserId, nominal, LocalDate.now(), catatan)
                        : new Pengeluaran(currentUserId, nominal, LocalDate.now(), catatan);

                transaksiService.tambahTransaksi(t);
            }

            // Refresh Dashboard
            if (dashboardController != null) {
                dashboardController.refreshFromPopup();
            }

            // Tutup Popup
            Stage stage = (Stage) txtNominal.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Nominal harus berupa angka").show();
        }
    }
}