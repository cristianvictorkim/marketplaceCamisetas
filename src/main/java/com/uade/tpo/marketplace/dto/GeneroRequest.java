package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.NotBlank;

public class GeneroRequest {

    @NotBlank
    private String nombre;

    public GeneroRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
