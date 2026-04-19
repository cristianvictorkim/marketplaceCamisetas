package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.NotBlank;

public class PedidoEstadoUpdateRequest {

    @NotBlank(message = "Estado is required")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

