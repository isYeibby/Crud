package com.crud.app;

// Usamos los imports de JAKARTA (el nuevo estándar de Java)
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * Esta clase es nuestra Entidad.
 * Hibernate la leerá y entenderá que es un espejo de la tabla 'usuarios'.
 */
@Entity // Le dice a JPA que esta clase debe ser persistida.
@Table(name = "usuarios") // Le dice el nombre de la tabla en el SQL.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    // --- IMPORTANTE ---
    // JPA/Hibernate necesita OBLIGATORIAMENTE un constructor vacío.
    public Usuario() {
    }

    // Constructor para crear nuevos usuarios
    public Usuario(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // --- Getters y Setters (Hibernate los usa para leer/escribir) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        // Un helper para mostrarlo bonito en la GUI
        return String.format("ID: %d | User: %s | Email: %s",
                id, username, email);
    }
}