package com.uade.tpo.marketplace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paises")
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(name = "grupo_mundial")
    private String grupoMundial;

    public Pais() {
    }

    public Pais(String nombre, String grupoMundial) {
        this.nombre = nombre;
        this.grupoMundial = grupoMundial;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoMundial() {
        return grupoMundial;
    }

    public void setGrupoMundial(String grupoMundial) {
        this.grupoMundial = grupoMundial;
    }
}
