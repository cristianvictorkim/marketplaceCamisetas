package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

    Optional<Genero> findFirstByNombre(String nombre);
}
