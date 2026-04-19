package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;

public class PedidoDetalleResponse {

    private Long id;
    private Long varianteId;
    private Long camisetaId;
    private String camisetaNombre;
    private String talle;
    private String color;
    private String sku;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public PedidoDetalleResponse(Long id,
                                 Long varianteId,
                                 Long camisetaId,
                                 String camisetaNombre,
                                 String talle,
                                 String color,
                                 String sku,
                                 Integer cantidad,
                                 BigDecimal precioUnitario,
                                 BigDecimal subtotal) {
        this.id = id;
        this.varianteId = varianteId;
        this.camisetaId = camisetaId;
        this.camisetaNombre = camisetaNombre;
        this.talle = talle;
        this.color = color;
        this.sku = sku;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public Long getVarianteId() {
        return varianteId;
    }

    public Long getCamisetaId() {
        return camisetaId;
    }

    public String getCamisetaNombre() {
        return camisetaNombre;
    }

    public String getTalle() {
        return talle;
    }

    public String getColor() {
        return color;
    }

    public String getSku() {
        return sku;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}

