package com.crud.app.postgresql.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;

@Entity(name = "ProductoTechPostgres")
@Table(name = "productos_tech")
public class ProductoTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "especificaciones", columnDefinition = "jsonb")
    private Map<String, Object> especificaciones;

    public ProductoTech() {}

    public ProductoTech(String nombre, Map<String, Object> especificaciones) {
        this.nombre = nombre;
        this.especificaciones = especificaciones;
    }

    public String getMarca() {
        return especificaciones != null ? (String) especificaciones.get("marca") : null;
    }

    public Integer getRam() {
        return especificaciones != null ? (Integer) especificaciones.get("ram") : null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<String, Object> getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(Map<String, Object> especificaciones) {
        this.especificaciones = especificaciones;
    }

    @Override
    public String toString() {
        return String.format("%s | Marca: %s | RAM: %d GB",
                nombre, getMarca(), getRam());
    }
}