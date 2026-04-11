package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;

public class CamisetaResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;
    private boolean activo;
    private String tipoCamiseta;
    private String genero;
    private String pais;

    public CamisetaResponse(Long id, String nombre, String descripcion, BigDecimal precio, String imagen,
                            boolean activo, String tipoCamiseta, String genero, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.activo = activo;
        this.tipoCamiseta = tipoCamiseta;
        this.genero = genero;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getTipoCamiseta() {
        return tipoCamiseta;
    }

    public String getGenero() {
        return genero;
    }

    public String getPais() {
        return pais;
    }
}
