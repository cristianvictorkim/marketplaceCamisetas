package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.UsuarioAdminCreateRequest;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.RolRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(
                usuarioRepository,
                rolRepository,
                carritoRepository,
                passwordEncoder
        );
    }

    @Test
    void rejectsUnsupportedAdminRole() {
        UsuarioAdminCreateRequest request = new UsuarioAdminCreateRequest();
        request.setNombre("Prueba");
        request.setApellido("Usuario");
        request.setEmail("prueba@example.com");
        request.setPassword("123456");
        request.setRol("SUPERADMIN");

        assertThrows(BusinessException.class, () -> usuarioService.adminCreate(request));

        verify(rolRepository, never()).findByNombre("SUPERADMIN");
        verify(usuarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
