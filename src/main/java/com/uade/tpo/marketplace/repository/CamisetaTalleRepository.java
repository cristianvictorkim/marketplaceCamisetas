package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.model.CamisetaTalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CamisetaTalleRepository extends JpaRepository<CamisetaTalle, Long> {

    List<CamisetaTalle> findByCamisetaId(Long camisetaId);
}
