package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.PedidoResponse;
import com.uade.tpo.marketplace.security.UsuarioPrincipal;
import com.uade.tpo.marketplace.service.PedidoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoResponse> getAll(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        return pedidoService.getAllForUser(usuarioPrincipal.getId());
    }

    @GetMapping("/{id}")
    public PedidoResponse getById(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
                                  @PathVariable Long id) {
        return pedidoService.getByIdForUser(id, usuarioPrincipal.getId());
    }
}
