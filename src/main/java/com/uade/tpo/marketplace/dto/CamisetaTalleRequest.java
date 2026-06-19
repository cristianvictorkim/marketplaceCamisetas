package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CamisetaTalleRequest {

    @NotNull
    private Long talleId;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotBlank
    @Size(max = 100)
    private String sku;

    @NotBlank
    @Size(max = 100)
    private String color;

    public CamisetaTalleRequest() {
    }

    public Long getTalleId() {
        return talleId;
    }

    public void setTalleId(Long talleId) {
        this.talleId = talleId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
