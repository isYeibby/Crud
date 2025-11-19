package com.crud.app.postgresql.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity(name = "EstudiantePostgres")
@Table(name = "estudiantes")
public class Estudiante extends Persona {

    @Column(name = "matricula", unique = true)
    private String matricula;

    @Column(name = "carrera")
    private String carrera;

    @Column(name = "promedio")
    private BigDecimal promedio;

    public Estudiante() {
        super();
    }

    public Estudiante(String nombre, Integer edad, String matricula, String carrera, BigDecimal promedio) {
        super(nombre, edad);
        this.matricula = matricula;
        this.carrera = carrera;
        this.promedio = promedio;
    }

    public String getEstadoBeca() {
        return promedio != null && promedio.compareTo(new BigDecimal("8.5")) >= 0
                ? "Con Beca"
                : "Sin Beca";
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return String.format("%s | Matrícula: %s | %s | Promedio: %.2f | %s",
                getNombre(), matricula, carrera, promedio, getEstadoBeca());
    }
}