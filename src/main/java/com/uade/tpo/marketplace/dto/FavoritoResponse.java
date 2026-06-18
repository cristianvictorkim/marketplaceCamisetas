package com.uade.tpo.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FavoritoResponse {

    private Long id;
    private Long camisetaId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;
    private String tipoCamiseta;
    private String genero;
    private String pais;
    private LocalDateTime fechaCreacion;

    public FavoritoResponse(Long id, Long camisetaId, String nombre, String descripcion,
                            BigDecimal precio, String imagen, String tipoCamiseta,
                            String genero, String pais, LocalDateTime fechaCreacion) {
        this.id = id;
        this.camisetaId = camisetaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.tipoCamiseta = tipoCamiseta;
        this.genero = genero;
        this.pais = pais;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public Long getCamisetaId() {
        return camisetaId;
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

    public String getTipoCamiseta() {
        return tipoCamiseta;
    }

    public String getGenero() {
        return genero;
    }

    public String getPais() {
        return pais;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
