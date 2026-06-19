package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Override
    @EntityGraph(attributePaths = "rol")
    List<Usuario> findAll();

    boolean existsByEmail(String email);

    boolean existsByRolNombre(String nombre);

    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findByEmail(String email);
}
