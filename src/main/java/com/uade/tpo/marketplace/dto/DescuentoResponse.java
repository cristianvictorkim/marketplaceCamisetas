package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DescuentoResponse {

    private Long id;
    private Long camisetaId;
    private BigDecimal porcentaje;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public DescuentoResponse(Long id, Long camisetaId, BigDecimal porcentaje, LocalDate fechaInicio, LocalDate fechaFin) {
        this.id = id;
        this.camisetaId = camisetaId;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getId() {
        return id;
    }

    public Long getCamisetaId() {
        return camisetaId;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }
}
