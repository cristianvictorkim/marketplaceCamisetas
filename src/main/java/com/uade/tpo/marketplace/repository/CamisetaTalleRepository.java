package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.CamisetaTalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CamisetaTalleRepository extends JpaRepository<CamisetaTalle, Long> {

    List<CamisetaTalle> findByCamisetaId(Long camisetaId);

    @Query("SELECT variante FROM CamisetaTalle variante " +
            "JOIN FETCH variante.camiseta " +
            "JOIN FETCH variante.talle")
    List<CamisetaTalle> findAllWithRelations();
}
