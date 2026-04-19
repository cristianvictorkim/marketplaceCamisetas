package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {

    private Long id;
    private Long usuarioId;
    private String usuarioEmail;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private List<PedidoDetalleResponse> detalles;

    public PedidoResponse(Long id,
                          Long usuarioId,
                          String usuarioEmail,
                          LocalDateTime fecha,
                          String estado,
                          BigDecimal total,
                          List<PedidoDetalleResponse> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioEmail = usuarioEmail;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<PedidoDetalleResponse> getDetalles() {
        return detalles;
    }
}

