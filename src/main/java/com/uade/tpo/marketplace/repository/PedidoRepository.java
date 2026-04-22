package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {
            "usuario",
            "detalles",
            "detalles.variante",
            "detalles.variante.camiseta",
            "detalles.variante.talle"
    })
    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    @EntityGraph(attributePaths = {
            "usuario",
            "detalles",
            "detalles.variante",
            "detalles.variante.camiseta",
            "detalles.variante.talle"
    })
    Optional<Pedido> findByIdAndUsuarioId(Long id, Long usuarioId);
}
