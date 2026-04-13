package com.uade.tpo.marketplace.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StockRequest {

    @NotNull
    @Min(0)
    private Integer stock;

    public StockRequest() {
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
