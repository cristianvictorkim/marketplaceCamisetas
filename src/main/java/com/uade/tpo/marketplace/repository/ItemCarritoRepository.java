package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    Optional<ItemCarrito> findByCarritoIdAndVarianteId(Long carritoId, Long varianteId);

    Optional<ItemCarrito> findByIdAndCarritoUsuarioId(Long id, Long usuarioId);
}
