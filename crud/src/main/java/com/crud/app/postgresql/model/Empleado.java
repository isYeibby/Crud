package com.crud.app.postgresql.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity(name = "EmpleadoPostgres")
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "nombre")
    private String nombre;

    @Embedded
    private Direccion direccion;

    @Column(name = "salario")
    private BigDecimal salario;

    public Empleado() {}

    public Empleado(String nombre, Direccion direccion, BigDecimal salario) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.salario = salario;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | Salario: €%.2f",
                idEmpleado, nombre,
                direccion != null ? direccion.getDireccionCompleta() : "Sin dirección",
                salario);
    }
}