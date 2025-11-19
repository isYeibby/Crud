package com.crud.app.postgresql.model;

import jakarta.persistence.*;

@Entity(name = "ProductoPostgres")
@Table(name = "productos")
public class ProductoCompuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private InfoProducto producto;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "activo")
    private Boolean activo = true;

    public ProductoCompuesto() {}

    public ProductoCompuesto(InfoProducto producto, String categoria) {
        this.producto = producto;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public InfoProducto getProducto() {
        return producto;
    }

    public void setProducto(InfoProducto producto) {
        this.producto = producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Precio IVA: $%.2f",
                categoria,
                producto.toString(),
                producto.getPrecioConIva());
    }
}