package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Carrito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @EntityGraph(attributePaths = {
            "usuario",
            "items",
            "items.variante",
            "items.variante.camiseta",
            "items.variante.talle"
    })
    Optional<Carrito> findByUsuarioId(Long usuarioId);
}
