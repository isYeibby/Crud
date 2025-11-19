package com.crud.app.oracle.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

/**
 * Tipo de objeto para Teléfono
 * Corresponde a tipo_telefono en colecciones.sql
 */
@Embeddable
public class Telefono {

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "NUMERO")
    private String numero;

    // Constructores
    public Telefono() {}

    public Telefono(String tipo, String numero) {
        this.tipo = tipo;
        this.numero = numero;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return tipo + ": " + numero;
    }
}