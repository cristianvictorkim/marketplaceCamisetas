package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.CarritoItemCantidadRequest;
import com.uade.tpo.marketplace.dto.CarritoItemRequest;
import com.uade.tpo.marketplace.dto.CarritoResponse;
import com.uade.tpo.marketplace.security.UsuarioPrincipal;
import com.uade.tpo.marketplace.service.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public CarritoResponse getCarrito(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        return carritoService.getCarrito(usuarioPrincipal.getId());
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponse addItem(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
                                   @Valid @RequestBody CarritoItemRequest request) {
        return carritoService.addItem(usuarioPrincipal.getId(), request);
    }

    @PatchMapping("/items/{id}")
    public CarritoResponse updateItem(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
                                      @PathVariable Long id,
                                      @Valid @RequestBody CarritoItemCantidadRequest request) {
        return carritoService.updateItem(usuarioPrincipal.getId(), id, request);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, @PathVariable Long id) {
        carritoService.deleteItem(usuarioPrincipal.getId(), id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        carritoService.clear(usuarioPrincipal.getId());
    }
}
