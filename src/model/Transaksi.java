package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public abstract class Transaksi {
    protected final IntegerProperty id;
    protected final IntegerProperty userId;
    protected final ObjectProperty<LocalDate> tanggal;
    protected final StringProperty jenis;
    protected final DoubleProperty nominal;
    protected final StringProperty catatan;
    
    public Transaksi(int userId, double nominal, LocalDate tanggal, String catatan) {
        this.id = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty(userId);
        this.nominal = new SimpleDoubleProperty(nominal);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
        this.catatan = new SimpleStringProperty(catatan);
        this.jenis = new SimpleStringProperty(""); 
    }
    
    public int getId() {
        return id.get();
    }

    public int getUserId() {
        return userId.get();
    }

    public LocalDate getTanggal() {
        return tanggal.get();
    }

    public String getJenis() {
        return jenis.get();
    }

    public double getNominal() {
        return nominal.get();
    }

    public String getCatatan() {
        return catatan.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal.set(tanggal);
    }

    public void setJenis(String jenis) {
        this.jenis.set(jenis);
    }

    public void setNominal(double nominal) {
        this.nominal.set(nominal);
    }

    public void setCatatan(String catatan) {
        this.catatan.set(catatan);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public ObjectProperty<LocalDate> tanggalProperty() {
        return tanggal;
    }

    public StringProperty jenisProperty() {
        return jenis;
    }

    public DoubleProperty nominalProperty() {
        return nominal;
    }

    public StringProperty catatanProperty() {
        return catatan;
    }
}