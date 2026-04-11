package com.uade.tpo.marketplace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipos_camiseta")
public class TipoCamiseta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private boolean titular;
    private boolean alternativa;

    public TipoCamiseta() {
    }

    public TipoCamiseta(String nombre, boolean titular, boolean alternativa) {
        this.nombre = nombre;
        this.titular = titular;
        this.alternativa = alternativa;
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

    public boolean isTitular() {
        return titular;
    }

    public void setTitular(boolean titular) {
        this.titular = titular;
    }

    public boolean isAlternativa() {
        return alternativa;
    }

    public void setAlternativa(boolean alternativa) {
        this.alternativa = alternativa;
    }
}
