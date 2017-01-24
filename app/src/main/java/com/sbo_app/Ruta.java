package com.sbo_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ruta {
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("idRuta")
    @Expose
    private Integer idRuta;
    @SerializedName("costo")
    @Expose
    private Integer costo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getCosto() {
        return costo;
    }
    public void setCosto(Integer costo){ this.costo = costo; }

}
