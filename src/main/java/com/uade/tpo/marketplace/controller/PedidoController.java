package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.PedidoEstadoUpdateRequest;
import com.uade.tpo.marketplace.dto.PedidoResponse;
import com.uade.tpo.marketplace.security.UsuarioPrincipal;
import com.uade.tpo.marketplace.service.PedidoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse create(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        return pedidoService.createFromCarrito(usuarioPrincipal.getId());
    }

    @GetMapping
    public List<PedidoResponse> list(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        return pedidoService.list(usuarioPrincipal.getId(), isAdmin(usuarioPrincipal));
    }

    @GetMapping("/{id}")
    public PedidoResponse getById(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, @PathVariable Long id) {
        return pedidoService.getById(id, usuarioPrincipal.getId(), isAdmin(usuarioPrincipal));
    }

    @PatchMapping("/{id}/cancelar")
    public PedidoResponse cancelar(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, @PathVariable Long id) {
        return pedidoService.cancel(id, usuarioPrincipal.getId(), isAdmin(usuarioPrincipal));
    }

    // ADMIN action (same base path, protected by SecurityConfig)
    @PatchMapping("/{id}/estado")
    public PedidoResponse updateEstado(@PathVariable Long id, @Valid @RequestBody PedidoEstadoUpdateRequest request) {
        return pedidoService.updateEstado(id, request);
    }

    private boolean isAdmin(UsuarioPrincipal usuarioPrincipal) {
        return usuarioPrincipal.getAuthorities()
                .stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}

