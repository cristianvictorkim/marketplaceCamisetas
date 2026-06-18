package com.uade.tpo.marketplace.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "favoritos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_favoritos_usuario_camiseta",
                columnNames = {"usuario_id", "camiseta_id"}
        )
)
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "camiseta_id", nullable = false)
    private Camiseta camiseta;

    private LocalDateTime fechaCreacion;

    public Favorito() {
    }

    public Favorito(Usuario usuario, Camiseta camiseta) {
        this.usuario = usuario;
        this.camiseta = camiseta;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Camiseta getCamiseta() {
        return camiseta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
