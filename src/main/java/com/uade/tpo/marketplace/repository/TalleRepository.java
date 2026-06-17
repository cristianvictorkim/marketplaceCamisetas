package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Talle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TalleRepository extends JpaRepository<Talle, Long> {

    Optional<Talle> findFirstByNombre(String nombre);
}
