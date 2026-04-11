package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.NotBlank;

public class TalleRequest {

    @NotBlank
    private String nombre;

    public TalleRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
