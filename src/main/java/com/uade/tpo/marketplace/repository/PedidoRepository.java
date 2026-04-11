package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
