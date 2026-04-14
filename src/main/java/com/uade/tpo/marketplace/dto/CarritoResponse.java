package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponse {

    private Long id;
    private Long usuarioId;
    private List<CarritoItemResponse> items;
    private BigDecimal total;

    public CarritoResponse(Long id, Long usuarioId, List<CarritoItemResponse> items, BigDecimal total) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.items = items;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public List<CarritoItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
