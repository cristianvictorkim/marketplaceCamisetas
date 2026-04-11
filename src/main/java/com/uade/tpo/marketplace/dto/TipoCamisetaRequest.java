package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.NotBlank;

public class TipoCamisetaRequest {

    @NotBlank
    private String nombre;

    private boolean titular;
    private boolean alternativa;

    public TipoCamisetaRequest() {
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
