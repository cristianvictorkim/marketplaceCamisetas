package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
