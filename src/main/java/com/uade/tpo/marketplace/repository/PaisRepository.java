package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaisRepository extends JpaRepository<Pais, Long> {

    Optional<Pais> findFirstByNombre(String nombre);
}
