package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.DetallePedidoResponse;
import com.uade.tpo.marketplace.dto.PedidoResponse;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.CamisetaTalle;
import com.uade.tpo.marketplace.model.Carrito;
import com.uade.tpo.marketplace.model.Descuento;
import com.uade.tpo.marketplace.model.DetallePedido;
import com.uade.tpo.marketplace.model.ItemCarrito;
import com.uade.tpo.marketplace.model.Pedido;
import com.uade.tpo.marketplace.repository.CamisetaTalleRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {

    private static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    private static final BigDecimal PORCENTAJE_DIVISOR = new BigDecimal("100");

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final CamisetaTalleRepository camisetaTalleRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         CarritoRepository carritoRepository,
                         CamisetaTalleRepository camisetaTalleRepository) {
        this.pedidoRepository = pedidoRepository;
        this.carritoRepository = carritoRepository;
        this.camisetaTalleRepository = camisetaTalleRepository;
    }

    public PedidoResponse checkout(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito not found for usuario with id " + usuarioId));

        if (carrito.getItems().isEmpty()) {
            throw new BusinessException("Carrito is empty");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(ESTADO_CONFIRMADO);

        BigDecimal total = BigDecimal.ZERO;
        LocalDate hoy = LocalDate.now();

        for (ItemCarrito item : carrito.getItems()) {
            CamisetaTalle variante = item.getVariante();
            Camiseta camiseta = variante.getCamiseta();

            validateCamisetaActiva(camiseta);
            validateStock(variante, item.getCantidad());

            BigDecimal precioUnitario = resolvePrecioUnitario(camiseta, hoy);
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setVariante(variante);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            detalle.setCategoria(camiseta.getTipoCamiseta().getNombre());
            pedido.getDetalles().add(detalle);

            variante.setStock(variante.getStock() - item.getCantidad());
            camisetaTalleRepository.save(variante);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        Pedido savedPedido = pedidoRepository.save(pedido);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return toResponse(savedPedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> getAllForUser(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponse getByIdForUser(Long id, Long usuarioId) {
        return toResponse(findPedidoDelUsuario(id, usuarioId));
    }

    private Pedido findPedidoDelUsuario(Long id, Long usuarioId) {
        return pedidoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id " + id));
    }

    private void validateCamisetaActiva(Camiseta camiseta) {
        if (!camiseta.isActivo()) {
            throw new BusinessException("Camiseta is not active");
        }
    }

    private void validateStock(CamisetaTalle variante, Integer cantidad) {
        if (cantidad > variante.getStock()) {
            throw new BusinessException("Requested quantity exceeds available stock");
        }
    }

    private BigDecimal resolvePrecioUnitario(Camiseta camiseta, LocalDate hoy) {
        BigDecimal precio = camiseta.getPrecio();
        Descuento descuento = camiseta.getDescuento();
        if (descuento == null) {
            return precio;
        }
        if (hoy.isBefore(descuento.getFechaInicio()) || hoy.isAfter(descuento.getFechaFin())) {
            return precio;
        }
        BigDecimal factor = BigDecimal.ONE.subtract(descuento.getPorcentaje().divide(PORCENTAJE_DIVISOR, 4, RoundingMode.HALF_UP));
        return precio.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<DetallePedidoResponse> detalles = pedido.getDetalles()
                .stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList());

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getTotal(),
                detalles
        );
    }

    private DetallePedidoResponse toDetalleResponse(DetallePedido detalle) {
        CamisetaTalle variante = detalle.getVariante();
        Camiseta camiseta = variante.getCamiseta();
        return new DetallePedidoResponse(
                detalle.getId(),
                variante.getId(),
                camiseta.getId(),
                camiseta.getNombre(),
                variante.getTalle().getNombre(),
                variante.getColor(),
                variante.getSku(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal(),
                detalle.getCategoria()
        );
    }
}
