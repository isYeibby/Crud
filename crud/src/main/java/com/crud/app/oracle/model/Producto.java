package com.crud.app.oracle.model;

import jakarta.persistence.*;

@Entity(name = "ProductoOracle")
@Table(name = "PRODUCTOS")
public class Producto {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "PRECIO")
    private Double precio;

    @Column(name = "STOCK")
    private Integer stock;

    public Producto() {}

    public Producto(Long id, String codigo, String nombre, Double precio, Integer stock) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Double getPrecioConIva() {
        return Math.round(precio * 1.21 * 100.0) / 100.0;
    }

    public String getInfoProducto() {
        return codigo + " - " + nombre + " ($" + precio + ")";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
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
        return String.format("%s | Stock: %d | Precio: $%.2f | Con IVA: $%.2f",
                getInfoProducto(), stock, precio, getPrecioConIva());
    }
}