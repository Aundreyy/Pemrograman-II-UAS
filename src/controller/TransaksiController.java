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


    
    @FXML
    public void handleTambahPemasukan() {
        simpan(true);
    }

    @FXML
    public void handleTambahPengeluaran() {
        simpan(false);
    }

    private void simpan(boolean isPemasukan) {
        try {
            double nominal = Double.parseDouble(txtNominal.getText());
            String catatan = txtCatatan.getText();

            if (transaksiEdit != null) {
                int idLama = transaksiEdit.getId();
                LocalDate tglLama = transaksiEdit.getTanggal();

                Transaksi tBaru;
                if (isPemasukan) {
                    tBaru = new Pemasukan(currentUserId, nominal, tglLama, catatan);
                } else {
                    tBaru = new Pengeluaran(currentUserId, nominal, tglLama, catatan);
                }

                tBaru.setId(idLama);

                transaksiService.updateTransaksi(tBaru);

            } else {
                Transaksi t = isPemasukan
                        ? new Pemasukan(currentUserId, nominal, LocalDate.now(), catatan)
                        : new Pengeluaran(currentUserId, nominal, LocalDate.now(), catatan);

                transaksiService.tambahTransaksi(t);
            }

            if (dashboardController != null) {
                dashboardController.refreshFromPopup();
            }

            Stage stage = (Stage) txtNominal.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Nominal harus berupa angka").show();
        }
    }
}