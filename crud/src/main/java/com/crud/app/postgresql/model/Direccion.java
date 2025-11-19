package com.crud.app.postgresql.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

@Embeddable
public class Direccion {

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    public Direccion() {}

    public Direccion(String calle, Integer numero, String ciudad, String codigoPostal) {
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    public String getDireccionCompleta() {
        return calle + " " + numero + ", " + codigoPostal + " " + ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return getDireccionCompleta();
    }
}