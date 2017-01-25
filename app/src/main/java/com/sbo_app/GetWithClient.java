package com.sbo_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWithClient {

    @SerializedName("primer_nombre")
    @Expose
    private String primerNombre;
    @SerializedName("primer_apellido")
    @Expose
    private String primerApellido;
    @SerializedName("id_tarjeta")
    @Expose
    private String idTarjeta;
    @SerializedName("saldo")
    @Expose
    private Integer saldo;
    @SerializedName("estado")
    @Expose
    private String estado;

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(String idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
