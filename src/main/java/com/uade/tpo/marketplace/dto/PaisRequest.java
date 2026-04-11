package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.NotBlank;

public class PaisRequest {

    @NotBlank
    private String nombre;

    private String grupoMundial;

    public PaisRequest() {
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
