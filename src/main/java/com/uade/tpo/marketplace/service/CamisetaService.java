package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.CamisetaRequest;
import com.uade.tpo.marketplace.dto.CamisetaCreateRequest;
import com.uade.tpo.marketplace.dto.CamisetaResponse;
import com.uade.tpo.marketplace.dto.CamisetaTalleRequest;
import com.uade.tpo.marketplace.dto.CamisetaTalleResponse;
import com.uade.tpo.marketplace.dto.DescuentoRequest;
import com.uade.tpo.marketplace.dto.DescuentoResponse;
import com.uade.tpo.marketplace.dto.StockRequest;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.CamisetaTalle;
import com.uade.tpo.marketplace.model.Descuento;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CamisetaService {

    private final CamisetaRepository camisetaRepository;
    private final TipoCamisetaRepository tipoCamisetaRepository;
    private final GeneroRepository generoRepository;
    private final PaisRepository paisRepository;
    private final TalleRepository talleRepository;
    private final CamisetaTalleRepository camisetaTalleRepository;
    private final DescuentoRepository descuentoRepository;

    public CamisetaService(CamisetaRepository camisetaRepository,
                           TipoCamisetaRepository tipoCamisetaRepository,
                           GeneroRepository generoRepository,
                           PaisRepository paisRepository,
                           TalleRepository talleRepository,
                           CamisetaTalleRepository camisetaTalleRepository,
                           DescuentoRepository descuentoRepository) {
        this.camisetaRepository = camisetaRepository;
        this.tipoCamisetaRepository = tipoCamisetaRepository;
        this.generoRepository = generoRepository;
        this.paisRepository = paisRepository;
        this.talleRepository = talleRepository;
        this.camisetaTalleRepository = camisetaTalleRepository;
        this.descuentoRepository = descuentoRepository;
    }

    @Transactional(readOnly = true)
    public List<CamisetaResponse> getAll(Long paisId,
                                         Long tipoCamisetaId,
                                         Long generoId,
                                         BigDecimal minPrecio,
                                         BigDecimal maxPrecio,
                                         String search,
                                         String talle,
                                         String sort) {
        validatePriceRange(minPrecio, maxPrecio);
        return camisetaRepository.search(
                        paisId,
                        tipoCamisetaId,
                        generoId,
                        minPrecio,
                        maxPrecio,
                        normalizeSearch(search),
                        normalizeSearch(talle)
                )
                .stream()
                .sorted(resolveSort(sort))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CamisetaResponse getById(Long id) {
        return toResponse(findCamiseta(id));
    }

    public CamisetaResponse create(CamisetaCreateRequest request) {
        TipoCamiseta tipoCamiseta = findTipoCamiseta(request.getTipoCamisetaId());
        Genero genero = findGenero(request.getGeneroId());
        Pais pais = findPais(request.getPaisId());
        validateVariantes(request.getVariantes());

        Camiseta camiseta = new Camiseta(
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getImagen(),
                tipoCamiseta,
                genero,
                pais
        );

        Camiseta saved = camisetaRepository.save(camiseta);
        List<CamisetaTalle> variantes = request.getVariantes()
                .stream()
                .map(varianteRequest -> new CamisetaTalle(
                        saved,
                        findTalle(varianteRequest.getTalleId()),
                        varianteRequest.getStock(),
                        varianteRequest.getSku(),
                        varianteRequest.getColor()
                ))
                .collect(Collectors.toList());
        camisetaTalleRepository.saveAll(variantes);

        return toResponse(saved);
    }

    public CamisetaResponse update(Long id, CamisetaRequest request) {
        Camiseta camiseta = findCamiseta(id);
        camiseta.setNombre(request.getNombre());
        camiseta.setDescripcion(request.getDescripcion());
        camiseta.setPrecio(request.getPrecio());
        camiseta.setImagen(request.getImagen());
        camiseta.setTipoCamiseta(findTipoCamiseta(request.getTipoCamisetaId()));
        camiseta.setGenero(findGenero(request.getGeneroId()));
        camiseta.setPais(findPais(request.getPaisId()));
        return toResponse(camisetaRepository.save(camiseta));
    }

    public void delete(Long id) {
        Camiseta camiseta = findCamiseta(id);
        camiseta.setActivo(false);
        camisetaRepository.save(camiseta);
    }

    @Transactional(readOnly = true)
    public List<CamisetaTalleResponse> getVariantesByCamiseta(Long camisetaId) {
        findCamiseta(camisetaId);
        return camisetaTalleRepository.findByCamisetaId(camisetaId)
                .stream()
                .map(this::toVarianteResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CamisetaTalleResponse> getAllVariantes() {
        return camisetaTalleRepository.findAllWithRelations()
                .stream()
                .map(this::toVarianteResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CamisetaTalleResponse getVarianteById(Long id) {
        return toVarianteResponse(findVariante(id));
    }

    public CamisetaTalleResponse createVariante(Long camisetaId, CamisetaTalleRequest request) {
        Camiseta camiseta = findCamiseta(camisetaId);
        Talle talle = findTalle(request.getTalleId());
        CamisetaTalle variante = new CamisetaTalle(camiseta, talle, request.getStock(), request.getSku(), request.getColor());
        return toVarianteResponse(camisetaTalleRepository.save(variante));
    }

    public CamisetaTalleResponse updateVariante(Long id, CamisetaTalleRequest request) {
        CamisetaTalle variante = findVariante(id);
        variante.setTalle(findTalle(request.getTalleId()));
        variante.setStock(request.getStock());
        variante.setSku(request.getSku());
        variante.setColor(request.getColor());
        return toVarianteResponse(camisetaTalleRepository.save(variante));
    }

    public CamisetaTalleResponse updateStock(Long id, StockRequest request) {
        CamisetaTalle variante = findVariante(id);
        variante.setStock(request.getStock());
        return toVarianteResponse(camisetaTalleRepository.save(variante));
    }

    public void deleteVariante(Long id) {
        CamisetaTalle variante = findVariante(id);
        camisetaTalleRepository.delete(variante);
    }

    public DescuentoResponse createDescuento(Long camisetaId, DescuentoRequest request) {
        Camiseta camiseta = findCamiseta(camisetaId);
        if (descuentoRepository.findByCamisetaId(camisetaId).isPresent()) {
            throw new BusinessException("Camiseta already has a descuento");
        }
        Descuento descuento = buildDescuento(camiseta, request);
        return toDescuentoResponse(descuentoRepository.save(descuento));
    }

    public DescuentoResponse updateDescuento(Long camisetaId, DescuentoRequest request) {
        findCamiseta(camisetaId);
        Descuento descuento = findDescuentoByCamiseta(camisetaId);
        applyDescuento(descuento, request);
        return toDescuentoResponse(descuentoRepository.save(descuento));
    }

    public void deleteDescuento(Long camisetaId) {
        findCamiseta(camisetaId);
        descuentoRepository.delete(findDescuentoByCamiseta(camisetaId));
    }

    private Camiseta findCamiseta(Long id) {
        return camisetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camiseta not found with id " + id));
    }

    private TipoCamiseta findTipoCamiseta(Long id) {
        return tipoCamisetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo camiseta not found with id " + id));
    }

    private Genero findGenero(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genero not found with id " + id));
    }

    private Pais findPais(Long id) {
        return paisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pais not found with id " + id));
    }

    private Talle findTalle(Long id) {
        return talleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talle not found with id " + id));
    }

    private CamisetaTalle findVariante(Long id) {
        return camisetaTalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variante not found with id " + id));
    }

    private Descuento findDescuentoByCamiseta(Long camisetaId) {
        return descuentoRepository.findByCamisetaId(camisetaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Descuento not found for camiseta with id " + camisetaId));
    }

    private void validatePriceRange(BigDecimal minPrecio, BigDecimal maxPrecio) {
        if (minPrecio != null && maxPrecio != null && minPrecio.compareTo(maxPrecio) > 0) {
            throw new BusinessException("Min precio cannot be greater than max precio");
        }
    }

    private void validateVariantes(List<CamisetaTalleRequest> variantes) {
        Set<Long> talleIds = new HashSet<Long>();
        Set<String> skus = new HashSet<String>();

        for (CamisetaTalleRequest variante : variantes) {
            if (!talleIds.add(variante.getTalleId())) {
                throw new BusinessException("Duplicate talle in variantes");
            }
            if (!skus.add(variante.getSku().trim().toLowerCase())) {
                throw new BusinessException("Duplicate SKU in variantes");
            }
        }
    }

    private String normalizeSearch(String search) {
        if (search == null || search.trim().isEmpty()) {
            return null;
        }
        return search.trim();
    }

    private Comparator<Camiseta> resolveSort(String sort) {
        if (sort == null || sort.trim().isEmpty() || "default".equalsIgnoreCase(sort)) {
            return Comparator.comparing(Camiseta::getId);
        }

        switch (sort.trim().toLowerCase()) {
            case "price-asc":
                return Comparator.comparing(Camiseta::getPrecio);
            case "price-desc":
                return Comparator.comparing(Camiseta::getPrecio).reversed();
            case "name-asc":
                return Comparator.comparing(Camiseta::getNombre, String.CASE_INSENSITIVE_ORDER);
            case "name-desc":
                return Comparator.comparing(Camiseta::getNombre, String.CASE_INSENSITIVE_ORDER).reversed();
            default:
                throw new BusinessException("Unsupported sort value: " + sort);
        }
    }

    private Descuento buildDescuento(Camiseta camiseta, DescuentoRequest request) {
        Descuento descuento = new Descuento();
        descuento.setCamiseta(camiseta);
        applyDescuento(descuento, request);
        return descuento;
    }

    private void applyDescuento(Descuento descuento, DescuentoRequest request) {
        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessException("Fecha fin must be after fecha inicio");
        }
        descuento.setPorcentaje(request.getPorcentaje());
        descuento.setFechaInicio(request.getFechaInicio());
        descuento.setFechaFin(request.getFechaFin());
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

    private CamisetaTalleResponse toVarianteResponse(CamisetaTalle variante) {
        return new CamisetaTalleResponse(
                variante.getId(),
                variante.getCamiseta().getId(),
                variante.getTalle().getNombre(),
                variante.getStock(),
                variante.getSku(),
                variante.getColor()
        );
    }

    private DescuentoResponse toDescuentoResponse(Descuento descuento) {
        return new DescuentoResponse(
                descuento.getId(),
                descuento.getCamiseta().getId(),
                descuento.getPorcentaje(),
                descuento.getFechaInicio(),
                descuento.getFechaFin()
        );
    }
}
