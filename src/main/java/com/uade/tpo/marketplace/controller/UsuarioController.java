package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.UsuarioAdminCreateRequest;
import com.uade.tpo.marketplace.dto.UsuarioAdminUpdateRequest;
import com.uade.tpo.marketplace.dto.UsuarioChangePasswordRequest;
import com.uade.tpo.marketplace.dto.UsuarioResponse;
import com.uade.tpo.marketplace.dto.UsuarioUpdateRequest;
import com.uade.tpo.marketplace.security.UsuarioPrincipal;
import com.uade.tpo.marketplace.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        return usuarioService.getMe(usuarioPrincipal.getId());
    }

    @PutMapping("/me")
    public UsuarioResponse updateMe(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
                                   @Valid @RequestBody UsuarioUpdateRequest request) {
        return usuarioService.updateMe(usuarioPrincipal.getId(), request);
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal,
                               @Valid @RequestBody UsuarioChangePasswordRequest request) {
        usuarioService.changePassword(usuarioPrincipal.getId(), request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateMe(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        usuarioService.deactivateMe(usuarioPrincipal.getId());
    }

    // ADMIN endpoints (same base path, protected by SecurityConfig)
    @GetMapping
    public List<UsuarioResponse> adminList() {
        return usuarioService.adminList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse adminGetById(@PathVariable Long id) {
        return usuarioService.adminGetById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse adminCreate(@Valid @RequestBody UsuarioAdminCreateRequest request) {
        return usuarioService.adminCreate(request);
    }

    @PutMapping("/{id}")
    public UsuarioResponse adminUpdate(@PathVariable Long id, @Valid @RequestBody UsuarioAdminUpdateRequest request) {
        return usuarioService.adminUpdate(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDeactivate(@PathVariable Long id) {
        usuarioService.adminDeactivate(id);
    }
}

