package com.crud.app.postgresql.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.math.BigDecimal;

/**
 * Tipo compuesto para información de producto
 * Corresponde a CREATE TYPE info_producto en tipos_compuestos.sql
 */
@Embeddable
public class InfoProducto {

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private BigDecimal precio;

    @Column(name = "stock")
    private Integer stock;

    // Constructores
    public InfoProducto() {}

    public InfoProducto(String codigo, String nombre, BigDecimal precio, Integer stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Función precio_con_iva (IVA 21%)
    public BigDecimal getPrecioConIva() {
        return precio.multiply(new BigDecimal("1.21")).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f) | Stock: %d", codigo, nombre, precio, stock);
    }
}