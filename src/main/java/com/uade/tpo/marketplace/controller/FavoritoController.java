package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.FavoritoExisteResponse;
import com.uade.tpo.marketplace.dto.FavoritoResponse;
import com.uade.tpo.marketplace.security.UsuarioPrincipal;
import com.uade.tpo.marketplace.service.FavoritoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @GetMapping
    public List<FavoritoResponse> list(
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        return favoritoService.list(usuarioPrincipal.getId());
    }

    @PostMapping("/{camisetaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoResponse add(
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
            @PathVariable Long camisetaId) {
        return favoritoService.add(usuarioPrincipal.getId(), camisetaId);
    }

    @DeleteMapping("/{camisetaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
            @PathVariable Long camisetaId) {
        favoritoService.remove(usuarioPrincipal.getId(), camisetaId);
    }

    @GetMapping("/{camisetaId}/existe")
    public FavoritoExisteResponse exists(
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
            @PathVariable Long camisetaId) {
        return favoritoService.exists(usuarioPrincipal.getId(), camisetaId);
    }
}
