package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.CamisetaRequest;
import com.uade.tpo.marketplace.dto.CamisetaResponse;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.model.Pais;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.repository.CamisetaRepository;
import com.uade.tpo.marketplace.repository.GeneroRepository;
import com.uade.tpo.marketplace.repository.PaisRepository;
import com.uade.tpo.marketplace.repository.TipoCamisetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CamisetaService {

    private final CamisetaRepository camisetaRepository;
    private final TipoCamisetaRepository tipoCamisetaRepository;
    private final GeneroRepository generoRepository;
    private final PaisRepository paisRepository;

    public CamisetaService(CamisetaRepository camisetaRepository,
                           TipoCamisetaRepository tipoCamisetaRepository,
                           GeneroRepository generoRepository,
                           PaisRepository paisRepository) {
        this.camisetaRepository = camisetaRepository;
        this.tipoCamisetaRepository = tipoCamisetaRepository;
        this.generoRepository = generoRepository;
        this.paisRepository = paisRepository;
    }

    @Transactional(readOnly = true)
    public List<CamisetaResponse> getAll() {
        return camisetaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CamisetaResponse getById(Long id) {
        Camiseta camiseta = camisetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camiseta not found with id " + id));
        return toResponse(camiseta);
    }

    public CamisetaResponse create(CamisetaRequest request) {
        TipoCamiseta tipoCamiseta = tipoCamisetaRepository.findById(request.getTipoCamisetaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo camiseta not found with id " + request.getTipoCamisetaId()));
        Genero genero = generoRepository.findById(request.getGeneroId())
                .orElseThrow(() -> new ResourceNotFoundException("Genero not found with id " + request.getGeneroId()));
        Pais pais = paisRepository.findById(request.getPaisId())
                .orElseThrow(() -> new ResourceNotFoundException("Pais not found with id " + request.getPaisId()));

        Camiseta camiseta = new Camiseta(
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getImagen(),
                tipoCamiseta,
                genero,
                pais
        );

        return toResponse(camisetaRepository.save(camiseta));
    }

    private CamisetaResponse toResponse(Camiseta camiseta) {
        return new CamisetaResponse(
                camiseta.getId(),
                camiseta.getNombre(),
                camiseta.getDescripcion(),
                camiseta.getPrecio(),
                camiseta.getImagen(),
                camiseta.isActivo(),
                camiseta.getTipoCamiseta().getNombre(),
                camiseta.getGenero().getNombre(),
                camiseta.getPais().getNombre()
        );
    }
}
