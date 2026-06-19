package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.UsuarioAdminCreateRequest;
import com.uade.tpo.marketplace.dto.UsuarioAdminUpdateRequest;
import com.uade.tpo.marketplace.dto.UsuarioChangePasswordRequest;
import com.uade.tpo.marketplace.dto.UsuarioResponse;
import com.uade.tpo.marketplace.dto.UsuarioUpdateRequest;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Carrito;
import com.uade.tpo.marketplace.model.Rol;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.RolRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    private static final String DEFAULT_USER_ROLE = "USER";
    private static final Set<String> ALLOWED_ROLES = new HashSet<String>(
            Arrays.asList("USER", "ADMIN")
    );

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          CarritoRepository carritoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.carritoRepository = carritoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UsuarioResponse getMe(Long usuarioId) {
        return toResponse(findUsuario(usuarioId));
    }

    public UsuarioResponse updateMe(Long usuarioId, UsuarioUpdateRequest request) {
        Usuario usuario = findUsuario(usuarioId);
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setDireccion(request.getDireccion());
        usuario.setTelefono(request.getTelefono());
        return toResponse(usuarioRepository.save(usuario));
    }

    public void changePassword(Long usuarioId, UsuarioChangePasswordRequest request) {
        Usuario usuario = findUsuario(usuarioId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getPassword())) {
            throw new BusinessException("Current password is invalid");
        }
        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);
    }

    public void deactivateMe(Long usuarioId) {
        Usuario usuario = findUsuario(usuarioId);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> adminList() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse adminGetById(Long id) {
        return toResponse(findUsuario(id));
    }

    public UsuarioResponse adminCreate(UsuarioAdminCreateRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }

        String roleName = normalizeRole(request.getRol());
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
        usuario.setActivo(true);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        Carrito carrito = new Carrito();
        carrito.setUsuario(savedUsuario);
        carritoRepository.save(carrito);

        return toResponse(savedUsuario);
    }

    public UsuarioResponse adminUpdate(Long id, UsuarioAdminUpdateRequest request) {
        Usuario usuario = findUsuario(id);

        String email = request.getEmail().trim().toLowerCase();
        if (!usuario.getEmail().equals(email) && usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(email);
        usuario.setDireccion(request.getDireccion());
        usuario.setTelefono(request.getTelefono());
        usuario.setActivo(request.isActivo());

        String roleName = normalizeRole(request.getRol());
        Rol rol = rolRepository.findByNombre(roleName)
                .orElseGet(() -> rolRepository.save(new Rol(roleName)));
        usuario.setRol(rol);

        return toResponse(usuarioRepository.save(usuario));
    }

    public void adminDeactivate(Long id) {
        Usuario usuario = findUsuario(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private Usuario findUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + id));
    }

    private String normalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return DEFAULT_USER_ROLE;
        }
        String normalized = role.trim().toUpperCase();
        if (!ALLOWED_ROLES.contains(normalized)) {
            throw new BusinessException("Unsupported role: " + normalized);
        }
        return normalized;
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.isActivo(),
                usuario.getRol().getNombre()
        );
    }
}

