package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {

    private Long id;
    private Long usuarioId;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private List<DetallePedidoResponse> detalles;

    public PedidoResponse(Long id, Long usuarioId, LocalDateTime fecha, String estado, BigDecimal total,
                          List<DetallePedidoResponse> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<DetallePedidoResponse> getDetalles() {
        return detalles;
    }
}
