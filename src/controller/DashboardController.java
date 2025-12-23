package controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Transaksi;
import service.TransaksiService;
import service.TransaksiService.DailyCheckResult;

public class DashboardController {

    @FXML private Label lblStatus;
    @FXML private Label lblTotalHarian;
    @FXML private Label lblPemasukan;
    @FXML private ProgressBar progressHarian;

    @FXML private TableView<Transaksi> tableTransaksi;
    @FXML private TableColumn<Transaksi, LocalDate> colTanggal;
    @FXML private TableColumn<Transaksi, String> colJenis;
    @FXML private TableColumn<Transaksi, Double> colNominal;
    @FXML private TableColumn<Transaksi, String> colCatatan;
    
    private int currentUserId;
    private final TransaksiService transaksiService = new TransaksiService();
    private final ObservableList<Transaksi> data = FXCollections.observableArrayList();
    private final NumberFormat rupiah = NumberFormat.getNumberInstance(Locale.of("id", "ID"));

    @FXML
    public void initialize() {
        // 1. SETUP KOLOM TABEL
        colTanggal.setCellValueFactory(c -> c.getValue().tanggalProperty());
        colJenis.setCellValueFactory(c -> c.getValue().jenisProperty());
        colCatatan.setCellValueFactory(c -> c.getValue().catatanProperty());
        
        colNominal.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) setText(null);
                else setText("Rp " + rupiah.format(value));
            }
        });
        colNominal.setCellValueFactory(c -> c.getValue().nominalProperty().asObject());

        tableTransaksi.setItems(data);

        tableTransaksi.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (newSelection instanceof model.Laporan) {
                    model.Laporan laporan = (model.Laporan) newSelection;
                    
                    lblStatus.setText(laporan.cetakLaporan());
                    
                    System.out.println("Output Interface: " + laporan.cetakLaporan());
                }
            }
        });
    }

    public void setUser(int userId) {
        this.currentUserId = userId;
        refreshDashboard();
    }

    private void refreshDashboard() {
    	List<Transaksi> list = transaksiService.getAllTransaksi(currentUserId);
        data.setAll(list);

        double totalPengeluaran = transaksiService.getTotalPengeluaranHarian(currentUserId, LocalDate.now());
        double totalPemasukan = transaksiService.getTotalPemasukanHarian(currentUserId, LocalDate.now());
        DailyCheckResult result = transaksiService.cekStatusHarian(currentUserId);

        lblTotalHarian.setText("Rp " + rupiah.format(totalPengeluaran) + " / Rp " + rupiah.format(result.limit));
        lblPemasukan.setText("Rp " + rupiah.format(totalPemasukan));

        updateStatus(result);
        
        double progress = result.limit == 0 ? 0 : totalPengeluaran / result.limit;
        progressHarian.setProgress(Math.min(progress, 1.0));
    }

    private void updateStatus(DailyCheckResult result) {
        lblStatus.setText("Status: " + result.status);
        lblStatus.getStyleClass().removeAll("status-aman", "status-waspada");
        if ("AMAN".equals(result.status)) lblStatus.getStyleClass().add("status-aman");
        else if ("WASPADA".equals(result.status)) lblStatus.getStyleClass().add("status-waspada");
    }

    @FXML
    public void handleTambahTransaksi() {
        openTransaksiPopup(null);
    }

    @FXML
    public void handleEditTransaksi() {
        Transaksi selected = tableTransaksi.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Pilih transaksi terlebih dahulu").show();
            return;
        }
        openTransaksiPopup(selected);
    }

    @FXML
    public void handleHapusTransaksi() {
        Transaksi t = tableTransaksi.getSelectionModel().getSelectedItem();
        if (t == null) {
            new Alert(Alert.AlertType.WARNING, "Pilih transaksi terlebih dahulu").show();
            return;
        }
        transaksiService.hapusTransaksi(t.getId());
        refreshDashboard();
    }
    
    @FXML
    public void handleWhatIf() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/what_if.fxml"));
            Parent root = loader.load();
            WhatIfController wc = loader.getController();
            wc.setUser(currentUserId);
            Stage stage = new Stage();
            stage.setTitle("Simulasi What If");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleSetDailyLimit() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atur Batas Harian");
        dialog.setHeaderText("Batas Pengeluaran Harian");
        dialog.setContentText("Masukkan batas (Rp):");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double limit = Double.parseDouble(input);
                if (limit > 0) {
                    transaksiService.updateDailyLimit(currentUserId, limit);
                    refreshDashboard();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Input harus angka").show();
            }
        });
    }

    private void openTransaksiPopup(Transaksi editData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/transaksi.fxml"));
            Parent root = loader.load();
            TransaksiController tc = loader.getController();
            tc.setUser(currentUserId);
            
            tc.setDashboardController(this);
            
            if (editData != null) {
                tc.setTransaksiEdit(editData);
            }
            Stage stage = new Stage();
            stage.setTitle(editData == null ? "Tambah Transaksi" : "Edit Transaksi");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal membuka form").show();
        }
    }

    public void refreshFromPopup() {
        refreshDashboard();
    }
}