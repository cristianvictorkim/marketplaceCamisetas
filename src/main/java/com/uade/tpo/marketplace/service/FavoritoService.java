package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.FavoritoExisteResponse;
import com.uade.tpo.marketplace.dto.FavoritoResponse;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.Favorito;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CamisetaRepository;
import com.uade.tpo.marketplace.repository.FavoritoRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CamisetaRepository camisetaRepository;

    public FavoritoService(FavoritoRepository favoritoRepository,
                           UsuarioRepository usuarioRepository,
                           CamisetaRepository camisetaRepository) {
        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.camisetaRepository = camisetaRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoritoResponse> list(Long usuarioId) {
        return favoritoRepository
                .findByUsuarioIdAndCamisetaActivoTrueOrderByFechaCreacionDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public FavoritoResponse add(Long usuarioId, Long camisetaId) {
        Favorito existing = favoritoRepository
                .findByUsuarioIdAndCamisetaId(usuarioId, camisetaId)
                .orElse(null);

        if (existing != null) {
            return toResponse(existing);
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + usuarioId));
        Camiseta camiseta = camisetaRepository.findById(camisetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Camiseta not found with id " + camisetaId));

        if (!camiseta.isActivo()) {
            throw new BusinessException("Camiseta is not active");
        }

        return toResponse(favoritoRepository.save(new Favorito(usuario, camiseta)));
    }

    public void remove(Long usuarioId, Long camisetaId) {
        favoritoRepository
                .findByUsuarioIdAndCamisetaId(usuarioId, camisetaId)
                .ifPresent(favoritoRepository::delete);
    }

    @Transactional(readOnly = true)
    public FavoritoExisteResponse exists(Long usuarioId, Long camisetaId) {
        return new FavoritoExisteResponse(
                camisetaId,
                favoritoRepository.existsByUsuarioIdAndCamisetaIdAndCamisetaActivoTrue(usuarioId, camisetaId)
        );
    }

    private FavoritoResponse toResponse(Favorito favorito) {
        Camiseta camiseta = favorito.getCamiseta();
        return new FavoritoResponse(
                favorito.getId(),
                camiseta.getId(),
                camiseta.getNombre(),
                camiseta.getDescripcion(),
                camiseta.getPrecio(),
                camiseta.getImagen(),
                camiseta.getTipoCamiseta().getNombre(),
                camiseta.getGenero().getNombre(),
                camiseta.getPais().getNombre(),
                favorito.getFechaCreacion()
        );
    }
}
