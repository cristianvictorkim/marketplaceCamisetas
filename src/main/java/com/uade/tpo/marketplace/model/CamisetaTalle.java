package com.uade.tpo.marketplace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "camiseta_talles")
public class CamisetaTalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camiseta_id", nullable = false)
    private Camiseta camiseta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talle_id", nullable = false)
    private Talle talle;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String color;

    public CamisetaTalle() {
    }

    public CamisetaTalle(Camiseta camiseta, Talle talle, Integer stock, String sku, String color) {
        this.camiseta = camiseta;
        this.talle = talle;
        this.stock = stock;
        this.sku = sku;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public Camiseta getCamiseta() {
        return camiseta;
    }

    public void setCamiseta(Camiseta camiseta) {
        this.camiseta = camiseta;
    }

    public Talle getTalle() {
        return talle;
    }

    public void setTalle(Talle talle) {
        this.talle = talle;
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
