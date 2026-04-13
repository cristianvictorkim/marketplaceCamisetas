package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.Camiseta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CamisetaRepository extends JpaRepository<Camiseta, Long> {

    @Query("SELECT c FROM Camiseta c " +
            "WHERE c.activo = true " +
            "AND (:paisId IS NULL OR c.pais.id = :paisId) " +
            "AND (:tipoCamisetaId IS NULL OR c.tipoCamiseta.id = :tipoCamisetaId) " +
            "AND (:generoId IS NULL OR c.genero.id = :generoId) " +
            "AND (:minPrecio IS NULL OR c.precio >= :minPrecio) " +
            "AND (:maxPrecio IS NULL OR c.precio <= :maxPrecio) " +
            "AND (:search IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Camiseta> search(
            @Param("paisId") Long paisId,
            @Param("tipoCamisetaId") Long tipoCamisetaId,
            @Param("generoId") Long generoId,
            @Param("minPrecio") BigDecimal minPrecio,
            @Param("maxPrecio") BigDecimal maxPrecio,
            @Param("search") String search
    );
}
