package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    boolean existsByRolNombre(String nombre);

    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findByEmail(String email);
}
