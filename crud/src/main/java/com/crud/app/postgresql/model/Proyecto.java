package com.crud.app.postgresql.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity(name = "ProyectoPostgres")
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tecnologias", columnDefinition = "text[]")
    private String[] tecnologias;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "miembros_equipo", columnDefinition = "integer[]")
    private Integer[] miembrosEquipo;

    public Proyecto() {}

    public Proyecto(String nombre, String[] tecnologias, Integer[] miembrosEquipo) {
        this.nombre = nombre;
        this.tecnologias = tecnologias;
        this.miembrosEquipo = miembrosEquipo;
    }

    public String getTecnologiasString() {
        return tecnologias != null ? String.join(", ", tecnologias) : "";
    }

    public int getNumTecnologias() {
        return tecnologias != null ? tecnologias.length : 0;
    }

    public int getNumMiembros() {
        return miembrosEquipo != null ? miembrosEquipo.length : 0;
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

    public String[] getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(String[] tecnologias) {
        this.tecnologias = tecnologias;
    }

    public Integer[] getMiembrosEquipo() {
        return miembrosEquipo;
    }

    public void setMiembrosEquipo(Integer[] miembrosEquipo) {
        this.miembrosEquipo = miembrosEquipo;
    }

    @Override
    public String toString() {
        return String.format("%s | Tecnologías: %d | Miembros: %d",
                nombre, getNumTecnologias(), getNumMiembros());
    }
}