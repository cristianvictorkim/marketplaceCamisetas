package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.AuthLoginRequest;
import com.uade.tpo.marketplace.dto.AuthRegisterRequest;
import com.uade.tpo.marketplace.dto.AuthResponse;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.model.Carrito;
import com.uade.tpo.marketplace.model.Rol;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.RolRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import com.uade.tpo.marketplace.security.JwtService;
import com.uade.tpo.marketplace.security.UsuarioPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private static final String DEFAULT_USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       CarritoRepository carritoRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.carritoRepository = carritoRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(AuthRegisterRequest request) {
        return registerWithRole(request, DEFAULT_USER_ROLE);
    }

    public AuthResponse bootstrapAdmin(AuthRegisterRequest request) {
        if (usuarioRepository.existsByRolNombre(ADMIN_ROLE)) {
            throw new BusinessException("Admin user already exists");
        }
        return registerWithRole(request, ADMIN_ROLE);
    }

    private AuthResponse registerWithRole(AuthRegisterRequest request, String roleName) {
        String email = request.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }

        Rol rol = rolRepository.findByNombre(roleName)
                .orElseGet(() -> rolRepository.save(new Rol(roleName)));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setDireccion(request.getDireccion());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(rol);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        Carrito carrito = new Carrito();
        carrito.setUsuario(savedUsuario);
        carritoRepository.save(carrito);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        return buildAuthResponse(authentication);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword())
        );
        return buildAuthResponse(authentication);
    }

    private AuthResponse buildAuthResponse(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new BusinessException("Authenticated user not found"));

        return new AuthResponse(token, usuario.getId(), usuario.getEmail(), usuario.getRol().getNombre());
    }
}
