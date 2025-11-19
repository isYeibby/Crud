package com.crud.app.oracle.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "PersonaOracle")
@Table(name = "PERSONAS")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "EMAIL")
    private String email;

    @ElementCollection
    @CollectionTable(name = "TABLA_TELEFONOS", joinColumns = @JoinColumn(name = "PERSONA_ID"))
    private List<Telefono> telefonos = new ArrayList<>();

    public Persona() {}

    public Persona(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public String presentarse() {
        return "Hola, soy " + nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }

    public void agregarTelefono(Telefono telefono) {
        this.telefonos.add(telefono);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | Teléfonos: %d",
                id, nombre, email, telefonos.size());
    }
}