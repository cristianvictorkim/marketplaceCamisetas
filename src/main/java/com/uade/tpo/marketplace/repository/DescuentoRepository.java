package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DescuentoRepository extends JpaRepository<Descuento, Long> {

    Optional<Descuento> findByCamisetaId(Long camisetaId);
}
