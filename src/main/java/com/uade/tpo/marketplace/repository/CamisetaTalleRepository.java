package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.CamisetaTalle;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface CamisetaTalleRepository extends JpaRepository<CamisetaTalle, Long> {

    List<CamisetaTalle> findByCamisetaId(Long camisetaId);

    boolean existsByCamisetaIdAndTalleId(Long camisetaId, Long talleId);

    boolean existsByCamisetaIdAndTalleIdAndIdNot(Long camisetaId, Long talleId, Long id);

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT variante FROM CamisetaTalle variante WHERE variante.id = :id")
    Optional<CamisetaTalle> findByIdForUpdate(@Param("id") Long id);

    @Query("SELECT variante FROM CamisetaTalle variante " +
            "JOIN FETCH variante.camiseta " +
            "JOIN FETCH variante.talle")
    List<CamisetaTalle> findAllWithRelations();
}
