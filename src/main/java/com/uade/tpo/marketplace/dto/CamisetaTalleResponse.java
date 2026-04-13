package com.uade.tpo.marketplace.dto;

public class CamisetaTalleResponse {

    private Long id;
    private Long camisetaId;
    private String talle;
    private Integer stock;
    private String sku;
    private String color;

    public CamisetaTalleResponse(Long id, Long camisetaId, String talle, Integer stock, String sku, String color) {
        this.id = id;
        this.camisetaId = camisetaId;
        this.talle = talle;
        this.stock = stock;
        this.sku = sku;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public Long getCamisetaId() {
        return camisetaId;
    }

    public String getTalle() {
        return talle;
    }

    public Integer getStock() {
        return stock;
    }

    public String getSku() {
        return sku;
    }

    public String getColor() {
        return color;
    }
}
