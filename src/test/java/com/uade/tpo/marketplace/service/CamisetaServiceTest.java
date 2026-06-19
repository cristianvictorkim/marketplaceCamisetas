package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.CamisetaCreateRequest;
import com.uade.tpo.marketplace.dto.CamisetaTalleRequest;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.model.Pais;
import com.uade.tpo.marketplace.model.Talle;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.repository.CamisetaRepository;
import com.uade.tpo.marketplace.repository.CamisetaTalleRepository;
import com.uade.tpo.marketplace.repository.DescuentoRepository;
import com.uade.tpo.marketplace.repository.GeneroRepository;
import com.uade.tpo.marketplace.repository.PaisRepository;
import com.uade.tpo.marketplace.repository.TalleRepository;
import com.uade.tpo.marketplace.repository.TipoCamisetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CamisetaServiceTest {

    @Mock
    private CamisetaRepository camisetaRepository;
    @Mock
    private TipoCamisetaRepository tipoCamisetaRepository;
    @Mock
    private GeneroRepository generoRepository;
    @Mock
    private PaisRepository paisRepository;
    @Mock
    private TalleRepository talleRepository;
    @Mock
    private CamisetaTalleRepository camisetaTalleRepository;
    @Mock
    private DescuentoRepository descuentoRepository;

    private CamisetaService camisetaService;

    @BeforeEach
    void setUp() {
        camisetaService = new CamisetaService(
                camisetaRepository,
                tipoCamisetaRepository,
                generoRepository,
                paisRepository,
                talleRepository,
                camisetaTalleRepository,
                descuentoRepository
        );
    }

    @Test
    void createPersistsAllVariantsInOneBatch() {
        TipoCamiseta tipo = new TipoCamiseta();
        tipo.setNombre("Titular");
        Genero genero = new Genero();
        genero.setNombre("Masculino");
        Pais pais = new Pais();
        pais.setNombre("Argentina");
        Talle small = talle("S");
        Talle medium = talle("M");
        CamisetaCreateRequest request = requestConVariantes();

        when(tipoCamisetaRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(generoRepository.findById(2L)).thenReturn(Optional.of(genero));
        when(paisRepository.findById(3L)).thenReturn(Optional.of(pais));
        when(talleRepository.findById(10L)).thenReturn(Optional.of(small));
        when(talleRepository.findById(11L)).thenReturn(Optional.of(medium));
        when(camisetaRepository.save(any(Camiseta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        camisetaService.create(request);

        ArgumentCaptor<List> variantsCaptor = ArgumentCaptor.forClass(List.class);
        verify(camisetaTalleRepository).saveAll(variantsCaptor.capture());
        assertEquals(2, variantsCaptor.getValue().size());
    }

    @Test
    void createRejectsDuplicateSkusBeforePersistingProduct() {
        TipoCamiseta tipo = new TipoCamiseta();
        Genero genero = new Genero();
        Pais pais = new Pais();
        CamisetaCreateRequest request = requestConVariantes();
        request.getVariantes().get(1).setSku("arg-s");

        when(tipoCamisetaRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(generoRepository.findById(2L)).thenReturn(Optional.of(genero));
        when(paisRepository.findById(3L)).thenReturn(Optional.of(pais));

        assertThrows(BusinessException.class, () -> camisetaService.create(request));

        verify(camisetaRepository, never()).save(any(Camiseta.class));
        verify(camisetaTalleRepository, never()).saveAll(any());
    }

    private CamisetaCreateRequest requestConVariantes() {
        CamisetaCreateRequest request = new CamisetaCreateRequest();
        request.setNombre("Camiseta Argentina");
        request.setDescripcion("Camiseta titular");
        request.setPrecio(new BigDecimal("120.00"));
        request.setImagen("https://example.com/argentina.png");
        request.setTipoCamisetaId(1L);
        request.setGeneroId(2L);
        request.setPaisId(3L);
        request.setVariantes(Arrays.asList(
                variante(10L, "ARG-S", 5),
                variante(11L, "ARG-M", 7)
        ));
        return request;
    }

    private CamisetaTalleRequest variante(Long talleId, String sku, int stock) {
        CamisetaTalleRequest request = new CamisetaTalleRequest();
        request.setTalleId(talleId);
        request.setSku(sku);
        request.setStock(stock);
        request.setColor("Celeste");
        return request;
    }

    private Talle talle(String nombre) {
        Talle talle = new Talle();
        talle.setNombre(nombre);
        return talle;
    }
}
