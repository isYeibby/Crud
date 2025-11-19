package com.crud.app.postgresql.model;

import jakarta.persistence.*;

@Entity(name = "PersonaPostgres")
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "edad")
    private Integer edad;

    public Persona() {}

    public Persona(String nombre, Integer edad) {
        this.nombre = nombre;
        this.edad = edad;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Edad: %d años", id, nombre, edad);
    }
}