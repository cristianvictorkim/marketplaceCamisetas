package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Favorito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    @EntityGraph(attributePaths = {
            "camiseta",
            "camiseta.tipoCamiseta",
            "camiseta.genero",
            "camiseta.pais"
    })
    List<Favorito> findByUsuarioIdAndCamisetaActivoTrueOrderByFechaCreacionDesc(Long usuarioId);

    Optional<Favorito> findByUsuarioIdAndCamisetaId(Long usuarioId, Long camisetaId);

    boolean existsByUsuarioIdAndCamisetaIdAndCamisetaActivoTrue(Long usuarioId, Long camisetaId);
}
