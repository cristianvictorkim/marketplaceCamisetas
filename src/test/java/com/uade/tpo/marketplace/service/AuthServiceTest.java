package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.AuthRegisterRequest;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.RolRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import com.uade.tpo.marketplace.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @Test
    void bootstrapAdminIsDisabledByDefaultConfiguration() {
        AuthService authService = new AuthService(
                usuarioRepository,
                rolRepository,
                carritoRepository,
                passwordEncoder,
                authenticationManager,
                jwtService,
                false
        );

        assertThrows(
                BusinessException.class,
                () -> authService.bootstrapAdmin(new AuthRegisterRequest())
        );

        verify(usuarioRepository, never()).existsByRolNombre("ADMIN");
    }
}
