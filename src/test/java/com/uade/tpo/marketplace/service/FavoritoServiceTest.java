package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.FavoritoExisteResponse;
import com.uade.tpo.marketplace.dto.FavoritoResponse;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.Favorito;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.model.Pais;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CamisetaRepository;
import com.uade.tpo.marketplace.repository.FavoritoRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CamisetaRepository camisetaRepository;

    @Mock
    private Usuario usuario;

    @Mock
    private Camiseta camiseta;

    @Mock
    private TipoCamiseta tipoCamiseta;

    @Mock
    private Genero genero;

    @Mock
    private Pais pais;

    private FavoritoService favoritoService;

    @BeforeEach
    void setUp() {
        favoritoService = new FavoritoService(
                favoritoRepository,
                usuarioRepository,
                camisetaRepository
        );
    }

    @Test
    void addCreatesFavoriteForActiveCamiseta() {
        Long usuarioId = 1L;
        Long camisetaId = 73L;
        Favorito favorito = configuredFavorito(camisetaId);

        when(favoritoRepository.findByUsuarioIdAndCamisetaId(usuarioId, camisetaId))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(camisetaRepository.findById(camisetaId)).thenReturn(Optional.of(camiseta));
        when(camiseta.isActivo()).thenReturn(true);
        when(favoritoRepository.save(any(Favorito.class))).thenReturn(favorito);

        FavoritoResponse response = favoritoService.add(usuarioId, camisetaId);

        assertEquals(camisetaId, response.getCamisetaId());
        assertEquals("Camiseta Austria", response.getNombre());
        verify(favoritoRepository).save(any(Favorito.class));
    }

    @Test
    void addIsIdempotentWhenFavoriteAlreadyExists() {
        Long usuarioId = 1L;
        Long camisetaId = 73L;
        Favorito favorito = configuredFavorito(camisetaId);

        when(favoritoRepository.findByUsuarioIdAndCamisetaId(usuarioId, camisetaId))
                .thenReturn(Optional.of(favorito));

        FavoritoResponse response = favoritoService.add(usuarioId, camisetaId);

        assertEquals(camisetaId, response.getCamisetaId());
        verify(favoritoRepository, never()).save(any(Favorito.class));
        verify(usuarioRepository, never()).findById(any(Long.class));
    }

    @Test
    void addRejectsInactiveCamiseta() {
        Long usuarioId = 1L;
        Long camisetaId = 73L;

        when(favoritoRepository.findByUsuarioIdAndCamisetaId(usuarioId, camisetaId))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(camisetaRepository.findById(camisetaId)).thenReturn(Optional.of(camiseta));
        when(camiseta.isActivo()).thenReturn(false);

        assertThrows(
                BusinessException.class,
                () -> favoritoService.add(usuarioId, camisetaId)
        );
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    @Test
    void removeDeletesExistingFavorite() {
        Long usuarioId = 1L;
        Long camisetaId = 73L;
        Favorito favorito = mock(Favorito.class);

        when(favoritoRepository.findByUsuarioIdAndCamisetaId(usuarioId, camisetaId))
                .thenReturn(Optional.of(favorito));

        favoritoService.remove(usuarioId, camisetaId);

        verify(favoritoRepository).delete(favorito);
    }

    @Test
    void removeDoesNothingWhenFavoriteDoesNotExist() {
        Long usuarioId = 1L;
        Long camisetaId = 73L;

        when(favoritoRepository.findByUsuarioIdAndCamisetaId(usuarioId, camisetaId))
                .thenReturn(Optional.empty());

        favoritoService.remove(usuarioId, camisetaId);

        verify(favoritoRepository, never()).delete(any(Favorito.class));
    }

    @Test
    void existsReturnsRepositoryResult() {
        when(favoritoRepository.existsByUsuarioIdAndCamisetaIdAndCamisetaActivoTrue(1L, 73L)).thenReturn(true);
        when(favoritoRepository.existsByUsuarioIdAndCamisetaIdAndCamisetaActivoTrue(1L, 74L)).thenReturn(false);

        FavoritoExisteResponse existing = favoritoService.exists(1L, 73L);
        FavoritoExisteResponse missing = favoritoService.exists(1L, 74L);

        assertTrue(existing.isFavorito());
        assertFalse(missing.isFavorito());
    }

    private Favorito configuredFavorito(Long camisetaId) {
        Favorito favorito = new Favorito(usuario, camiseta);

        when(camiseta.getId()).thenReturn(camisetaId);
        when(camiseta.getNombre()).thenReturn("Camiseta Austria");
        when(camiseta.getDescripcion()).thenReturn("Descripcion");
        when(camiseta.getPrecio()).thenReturn(new BigDecimal("100.00"));
        when(camiseta.getImagen()).thenReturn("https://example.com/camiseta.png");
        when(camiseta.getTipoCamiseta()).thenReturn(tipoCamiseta);
        when(camiseta.getGenero()).thenReturn(genero);
        when(camiseta.getPais()).thenReturn(pais);
        when(tipoCamiseta.getNombre()).thenReturn("Titular");
        when(genero.getNombre()).thenReturn("Unisex");
        when(pais.getNombre()).thenReturn("Austria");

        return favorito;
    }
}
