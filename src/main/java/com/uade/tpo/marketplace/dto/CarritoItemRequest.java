package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CarritoItemRequest {

    @NotNull(message = "Variante id is required")
    private Long varianteId;

    @NotNull(message = "Cantidad is required")
    @Min(value = 1, message = "Cantidad must be greater than zero")
    private Integer cantidad;

    public Long getVarianteId() {
        return varianteId;
    }

    public void setVarianteId(Long varianteId) {
        this.varianteId = varianteId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
