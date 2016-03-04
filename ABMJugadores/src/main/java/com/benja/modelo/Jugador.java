package com.benja.modelo;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

/**
 * Created by benjamin.salas on 13/04/2015.
 */

@Entity
@Table(name = "jugador")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jugador_id")
    private long id;

    @NotNull
    @Column(name = "jugador_nombre")
    private String nombre;

    @NotNull
    @Column(name = "jugador_apellido")
    private String apellido;

    @NotNull
    @Column(name = "jugador_fecha_nac")
    private String fechaNac;

    @NotNull
    @Column(name = "jugador_posicion")
    private String posicion;

    public Jugador() {
    }

    public Jugador(long id) {
        this.id = id;
    }

    public Jugador(String nombre, String apellido, String fechaNac, String posicion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNac = fechaNac;
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNac='" + fechaNac + '\'' +
                ", posicion='" + posicion + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
}
