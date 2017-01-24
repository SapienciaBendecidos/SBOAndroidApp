package com.sbo_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripRequest {
    @SerializedName("idRuta")
    @Expose
    private Integer idRuta;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("busPlaca")
    @Expose
    private String busPlaca;
    @SerializedName("tipoMovimiento")
    @Expose
    private String tipoMovimiento;
    @SerializedName("busConductor")
    @Expose
    private String busConductor;
    @SerializedName("transacciones")
    @Expose
    private List<String> transacciones = null;

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getBusPlaca() {
        return busPlaca;
    }

    public void setBusPlaca(String busPlaca) {
        this.busPlaca = busPlaca;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getBusConductor() {
        return busConductor;
    }

    public void setBusConductor(String busConductor) {
        this.busConductor = busConductor;
    }

    public List<String> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<String> transacciones) {
        this.transacciones = transacciones;
    }
}
