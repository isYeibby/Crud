package com.crud.app.oracle.model;

import jakarta.persistence.*;

@Entity(name = "EmpleadoOracle")
@Table(name = "EMPLEADOS")
public class Empleado {

    @Id
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;

    @Column(name = "NOMBRE")
    private String nombre;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "DIRECCION_CALLE")),
            @AttributeOverride(name = "numero", column = @Column(name = "DIRECCION_NUMERO")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "DIRECCION_CIUDAD")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "DIRECCION_CODIGO_POSTAL"))
    })
    private Direccion direccion;

    @Column(name = "SALARIO")
    private Double salario;

    public Empleado() {}

    public Empleado(Long idEmpleado, String nombre, Direccion direccion, Double salario) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.direccion = direccion;
        this.salario = salario;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
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

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
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