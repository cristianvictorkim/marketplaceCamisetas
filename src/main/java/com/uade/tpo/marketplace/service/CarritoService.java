package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.CarritoItemCantidadRequest;
import com.uade.tpo.marketplace.dto.CarritoItemRequest;
import com.uade.tpo.marketplace.dto.CarritoItemResponse;
import com.uade.tpo.marketplace.dto.CarritoResponse;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.CamisetaTalle;
import com.uade.tpo.marketplace.model.Carrito;
import com.uade.tpo.marketplace.model.ItemCarrito;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CamisetaTalleRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.ItemCarritoRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final CamisetaTalleRepository camisetaTalleRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          ItemCarritoRepository itemCarritoRepository,
                          CamisetaTalleRepository camisetaTalleRepository,
                          UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.camisetaTalleRepository = camisetaTalleRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public CarritoResponse getCarrito(Long usuarioId) {
        return toResponse(findOrCreateCarrito(usuarioId));
    }

    public CarritoResponse addItem(Long usuarioId, CarritoItemRequest request) {
        Carrito carrito = findOrCreateCarrito(usuarioId);
        CamisetaTalle variante = findVariante(request.getVarianteId());
        validateCamisetaActiva(variante);

        ItemCarrito item = itemCarritoRepository
                .findByCarritoIdAndVarianteId(carrito.getId(), variante.getId())
                .orElseGet(ItemCarrito::new);

        int cantidadActual = item.getId() == null ? 0 : item.getCantidad();
        int nuevaCantidad = cantidadActual + request.getCantidad();
        validateStock(variante, nuevaCantidad);

        if (item.getId() == null) {
            item.setCarrito(carrito);
            item.setVariante(variante);
        }
        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);

        return toResponse(findOrCreateCarrito(usuarioId));
    }

    public CarritoResponse updateItem(Long usuarioId, Long itemId, CarritoItemCantidadRequest request) {
        ItemCarrito item = findItemDelUsuario(itemId, usuarioId);
        validateCamisetaActiva(item.getVariante());
        validateStock(item.getVariante(), request.getCantidad());
        item.setCantidad(request.getCantidad());
        itemCarritoRepository.save(item);
        return toResponse(findOrCreateCarrito(usuarioId));
    }

    public void deleteItem(Long usuarioId, Long itemId) {
        ItemCarrito item = findItemDelUsuario(itemId, usuarioId);
        itemCarritoRepository.delete(item);
    }

    public void clear(Long usuarioId) {
        Carrito carrito = findOrCreateCarrito(usuarioId);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }

    private Carrito findOrCreateCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> createCarrito(usuarioId));
    }

    private Carrito createCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + usuarioId));
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        return carritoRepository.save(carrito);
    }

    private CamisetaTalle findVariante(Long varianteId) {
        return camisetaTalleRepository.findById(varianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Variante not found with id " + varianteId));
    }

    private ItemCarrito findItemDelUsuario(Long itemId, Long usuarioId) {
        return itemCarritoRepository.findByIdAndCarritoUsuarioId(itemId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Item carrito not found with id " + itemId));
    }

    private void validateCamisetaActiva(CamisetaTalle variante) {
        if (!variante.getCamiseta().isActivo()) {
            throw new BusinessException("Camiseta is not active");
        }
    }

    private void validateStock(CamisetaTalle variante, Integer cantidad) {
        if (cantidad > variante.getStock()) {
            throw new BusinessException("Requested quantity exceeds available stock");
        }
    }

    private CarritoResponse toResponse(Carrito carrito) {
        List<CarritoItemResponse> items = carrito.getItems()
                .stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoResponse(carrito.getId(), carrito.getUsuario().getId(), items, total);
    }

    private CarritoItemResponse toItemResponse(ItemCarrito item) {
        BigDecimal precioUnitario = item.getVariante().getCamiseta().getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

        return new CarritoItemResponse(
                item.getId(),
                item.getVariante().getId(),
                item.getVariante().getCamiseta().getId(),
                item.getVariante().getCamiseta().getNombre(),
                item.getVariante().getTalle().getNombre(),
                item.getVariante().getColor(),
                item.getVariante().getSku(),
                item.getCantidad(),
                precioUnitario,
                subtotal
        );
    }
}
