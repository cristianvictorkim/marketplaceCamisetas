package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.CamisetaRequest;
import com.uade.tpo.marketplace.dto.CamisetaResponse;
import com.uade.tpo.marketplace.dto.CamisetaTalleRequest;
import com.uade.tpo.marketplace.dto.CamisetaTalleResponse;
import com.uade.tpo.marketplace.dto.DescuentoRequest;
import com.uade.tpo.marketplace.dto.DescuentoResponse;
import com.uade.tpo.marketplace.dto.StockRequest;
import com.uade.tpo.marketplace.service.CamisetaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/camisetas")
public class CamisetaController {

    private final CamisetaService camisetaService;

    public CamisetaController(CamisetaService camisetaService) {
        this.camisetaService = camisetaService;
    }

    @GetMapping
    public List<CamisetaResponse> getAll(@RequestParam(required = false) Long paisId,
                                         @RequestParam(required = false) Long tipoCamisetaId,
                                         @RequestParam(required = false) Long generoId,
                                         @RequestParam(required = false) BigDecimal minPrecio,
                                         @RequestParam(required = false) BigDecimal maxPrecio,
                                         @RequestParam(required = false) String search) {
        return camisetaService.getAll(paisId, tipoCamisetaId, generoId, minPrecio, maxPrecio, search);
    }

    @GetMapping("/{id}")
    public CamisetaResponse getById(@PathVariable Long id) {
        return camisetaService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CamisetaResponse create(@Valid @RequestBody CamisetaRequest request) {
        return camisetaService.create(request);
    }

    @PutMapping("/{id}")
    public CamisetaResponse update(@PathVariable Long id, @Valid @RequestBody CamisetaRequest request) {
        return camisetaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        camisetaService.delete(id);
    }

    @GetMapping("/{id}/variantes")
    public List<CamisetaTalleResponse> getVariantes(@PathVariable Long id) {
        return camisetaService.getVariantesByCamiseta(id);
    }

    @PostMapping("/{id}/variantes")
    @ResponseStatus(HttpStatus.CREATED)
    public CamisetaTalleResponse createVariante(@PathVariable Long id,
                                                @Valid @RequestBody CamisetaTalleRequest request) {
        return camisetaService.createVariante(id, request);
    }

    @GetMapping("/variantes/{id}")
    public CamisetaTalleResponse getVarianteById(@PathVariable Long id) {
        return camisetaService.getVarianteById(id);
    }

    @PutMapping("/variantes/{id}")
    public CamisetaTalleResponse updateVariante(@PathVariable Long id,
                                                @Valid @RequestBody CamisetaTalleRequest request) {
        return camisetaService.updateVariante(id, request);
    }

    @PatchMapping("/variantes/{id}/stock")
    public CamisetaTalleResponse updateStock(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        return camisetaService.updateStock(id, request);
    }

    @DeleteMapping("/variantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVariante(@PathVariable Long id) {
        camisetaService.deleteVariante(id);
    }

    @PostMapping("/{id}/descuento")
    @ResponseStatus(HttpStatus.CREATED)
    public DescuentoResponse createDescuento(@PathVariable Long id, @Valid @RequestBody DescuentoRequest request) {
        return camisetaService.createDescuento(id, request);
    }

    @PutMapping("/{id}/descuento")
    public DescuentoResponse updateDescuento(@PathVariable Long id, @Valid @RequestBody DescuentoRequest request) {
        return camisetaService.updateDescuento(id, request);
    }

    @DeleteMapping("/{id}/descuento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDescuento(@PathVariable Long id) {
        camisetaService.deleteDescuento(id);
    }
}
