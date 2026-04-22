package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;

public class DetallePedidoResponse {

    private Long id;
    private Long varianteId;
    private Long camisetaId;
    private String camiseta;
    private String talle;
    private String color;
    private String sku;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String categoria;

    public DetallePedidoResponse(Long id, Long varianteId, Long camisetaId, String camiseta, String talle,
                                 String color, String sku, Integer cantidad, BigDecimal precioUnitario,
                                 BigDecimal subtotal, String categoria) {
        this.id = id;
        this.varianteId = varianteId;
        this.camisetaId = camisetaId;
        this.camiseta = camiseta;
        this.talle = talle;
        this.color = color;
        this.sku = sku;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.categoria = categoria;
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

    public String getCamiseta() {
        return camiseta;
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

    public String getCategoria() {
        return categoria;
    }
}
