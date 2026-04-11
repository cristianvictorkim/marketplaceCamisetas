package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.CamisetaRequest;
import com.uade.tpo.marketplace.dto.CamisetaResponse;
import com.uade.tpo.marketplace.service.CamisetaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/camisetas")
public class CamisetaController {

    private final CamisetaService camisetaService;

    public CamisetaController(CamisetaService camisetaService) {
        this.camisetaService = camisetaService;
    }

    @GetMapping
    public List<CamisetaResponse> getAll() {
        return camisetaService.getAll();
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
}
