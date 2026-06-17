package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.TipoCamiseta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoCamisetaRepository extends JpaRepository<TipoCamiseta, Long> {

    Optional<TipoCamiseta> findFirstByNombre(String nombre);
}
