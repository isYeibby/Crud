package com.crud.app.oracle.model;

import jakarta.persistence.*;

@Entity(name = "EstudianteOracle")
@Table(name = "TABLA_ESTUDIANTES")
public class Estudiante extends Persona {

    @Column(name = "EDAD")
    private Integer edad;

    @Column(name = "MATRICULA")
    private String matricula;

    @Column(name = "CARRERA")
    private String carrera;

    @Column(name = "PROMEDIO")
    private Double promedio;

    public Estudiante() {
        super();
    }

    public Estudiante(Long id, String nombre, Integer edad, String matricula, String carrera, Double promedio) {
        super(id, nombre, null);
        this.edad = edad;
        this.matricula = matricula;
        this.carrera = carrera;
        this.promedio = promedio;
    }

    @Override
    public String presentarse() {
        return String.format("Estudiante: %s, Matrícula: %s, Carrera: %s, Promedio: %.2f",
                getNombre(), matricula, carrera, promedio);
    }

    public String tieneBeca() {
        return promedio >= 8.5 ? "SÍ" : "NO";
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
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

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return String.format("%s | Edad: %d | Matrícula: %s | %s | Promedio: %.2f | Beca: %s",
                getNombre(), edad, matricula, carrera, promedio, tieneBeca());
    }
}