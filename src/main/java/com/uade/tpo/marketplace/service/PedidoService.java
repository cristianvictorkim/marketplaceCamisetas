package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.PedidoDetalleResponse;
import com.uade.tpo.marketplace.dto.PedidoEstadoUpdateRequest;
import com.uade.tpo.marketplace.dto.PedidoResponse;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Carrito;
import com.uade.tpo.marketplace.model.CamisetaTalle;
import com.uade.tpo.marketplace.model.DetallePedido;
import com.uade.tpo.marketplace.model.ItemCarrito;
import com.uade.tpo.marketplace.model.Pedido;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CamisetaTalleRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.PedidoRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final CamisetaTalleRepository camisetaTalleRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         CarritoRepository carritoRepository,
                         CamisetaTalleRepository camisetaTalleRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.camisetaTalleRepository = camisetaTalleRepository;
    }

    public PedidoResponse createFromCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + usuarioId));

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito not found for usuario with id " + usuarioId));

        if (carrito.getItems().isEmpty()) {
            throw new BusinessException("Carrito is empty");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(ESTADO_PENDIENTE);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrito item : carrito.getItems()) {
            CamisetaTalle variante = camisetaTalleRepository.findByIdForUpdate(item.getVariante().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variante not found with id " + item.getVariante().getId()));

            if (!variante.getCamiseta().isActivo()) {
                throw new BusinessException("Camiseta is not active");
            }

            if (item.getCantidad() > variante.getStock()) {
                throw new BusinessException("Requested quantity exceeds available stock");
            }

            BigDecimal precioUnitario = variante.getCamiseta().getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setVariante(variante);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            detalle.setCategoria(variante.getCamiseta().getTipoCamiseta().getNombre());

            pedido.getDetalles().add(detalle);

            variante.setStock(variante.getStock() - item.getCantidad());
            camisetaTalleRepository.save(variante);
        }

        pedido.setTotal(total);
        Pedido saved = pedidoRepository.save(pedido);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> list(Long usuarioId, boolean isAdmin) {
        if (isAdmin) {
            return pedidoRepository.findAllByOrderByFechaDesc()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponse getById(Long pedidoId, Long usuarioId, boolean isAdmin) {
        Pedido pedido;
        if (isAdmin) {
            pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id " + pedidoId));
        } else {
            pedido = pedidoRepository.findByIdAndUsuarioId(pedidoId, usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id " + pedidoId));
        }
        return toResponse(pedido);
    }

    public PedidoResponse cancel(Long pedidoId, Long usuarioId, boolean isAdmin) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id " + pedidoId));

        if (!isAdmin && !pedido.getUsuario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Pedido not found with id " + pedidoId);
        }

        if (ESTADO_CANCELADO.equalsIgnoreCase(pedido.getEstado())) {
            return toResponse(pedido);
        }

        if (!ESTADO_PENDIENTE.equalsIgnoreCase(pedido.getEstado())) {
            throw new BusinessException("Pedido cannot be cancelled in its current state");
        }

        cancelarYReponerStock(pedido);
        return toResponse(pedidoRepository.save(pedido));
    }

    public PedidoResponse updateEstado(Long pedidoId, PedidoEstadoUpdateRequest request) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id " + pedidoId));

        String estado = request.getEstado().trim().toUpperCase();
        if (estado.isEmpty()) {
            throw new BusinessException("Estado is required");
        }

        if (ESTADO_CANCELADO.equalsIgnoreCase(pedido.getEstado())) {
            if (ESTADO_CANCELADO.equals(estado)) {
                return toResponse(pedido);
            }
            throw new BusinessException("Pedido cancelado cannot change state");
        }

        if (ESTADO_CANCELADO.equals(estado)
                && !ESTADO_CANCELADO.equalsIgnoreCase(pedido.getEstado())) {
            cancelarYReponerStock(pedido);
        } else {
            pedido.setEstado(estado);
        }
        return toResponse(pedidoRepository.save(pedido));
    }

    private void cancelarYReponerStock(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            CamisetaTalle variante = detalle.getVariante();
            variante.setStock(variante.getStock() + detalle.getCantidad());
            camisetaTalleRepository.save(variante);
        }
        pedido.setEstado(ESTADO_CANCELADO);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<PedidoDetalleResponse> detalles = pedido.getDetalles()
                .stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList());

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getUsuario().getEmail(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getTotal(),
                detalles
        );
    }

    private PedidoDetalleResponse toDetalleResponse(DetallePedido detalle) {
        CamisetaTalle variante = detalle.getVariante();
        return new PedidoDetalleResponse(
                detalle.getId(),
                variante.getId(),
                variante.getCamiseta().getId(),
                variante.getCamiseta().getNombre(),
                variante.getTalle().getNombre(),
                variante.getColor(),
                variante.getSku(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}

