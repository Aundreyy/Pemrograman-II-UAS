package controller;

import java.text.NumberFormat;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WhatIfController {
    @FXML private TextField txtKeyword;
    @FXML private TextField txtNominal;
    @FXML private Label lblHasil;
    
    private int currentUserId;

    public void setUser(int userId) {
    	this.currentUserId = userId;
    }

    @FXML
    public void handleSimulasi() { 
        try {
        	System.out.println("Melakukan simulasi untuk User ID: " + currentUserId);
        	
            String keyword = txtKeyword.getText().trim();
            double nominal = Double.parseDouble(txtNominal.getText());

            if (keyword.isEmpty() || nominal <= 0) {
                throw new IllegalArgumentException();
            }

            double estimasiUser = nominal * 30;

            lblHasil.setText(
                "📊 Analisis pengeluaran \"" + keyword + "\"\n\n" +
                "🔸 Rencana kamu:\n" +
                "   • Input: Rp " + rupiah(nominal) + " / hari\n" +
                "   • Estimasi: Rp " + rupiah(estimasiUser) + " / bulan"
            );

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Masukkan keyword & nominal yang valid").show();
        }
    }

    private String rupiah(double value) {
    	NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        return nf.format(value);
    }
}