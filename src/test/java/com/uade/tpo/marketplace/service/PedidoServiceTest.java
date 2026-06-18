package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.dto.PedidoEstadoUpdateRequest;
import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.CamisetaTalle;
import com.uade.tpo.marketplace.model.Carrito;
import com.uade.tpo.marketplace.model.DetallePedido;
import com.uade.tpo.marketplace.model.ItemCarrito;
import com.uade.tpo.marketplace.model.Pedido;
import com.uade.tpo.marketplace.model.Talle;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.model.Usuario;
import com.uade.tpo.marketplace.repository.CamisetaTalleRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.PedidoRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private CamisetaTalleRepository camisetaTalleRepository;

    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService(
                pedidoRepository,
                usuarioRepository,
                carritoRepository,
                camisetaTalleRepository
        );
    }

    @Test
    void adminCancellationRestoresOrderStock() {
        Pedido pedido = pedidoConUnaVariante("PENDIENTE", 7, 3);
        CamisetaTalle variante = pedido.getDetalles().get(0).getVariante();
        PedidoEstadoUpdateRequest request = estado("CANCELADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        pedidoService.updateEstado(1L, request);

        assertEquals(10, variante.getStock());
        assertEquals("CANCELADO", pedido.getEstado());
        verify(camisetaTalleRepository).save(variante);
    }

    @Test
    void cancellingAlreadyCancelledOrderDoesNotRestoreStockTwice() {
        Pedido pedido = pedidoConUnaVariante("CANCELADO", 10, 3);
        CamisetaTalle variante = pedido.getDetalles().get(0).getVariante();
        PedidoEstadoUpdateRequest request = estado("CANCELADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.updateEstado(1L, request);

        assertEquals(10, variante.getStock());
        verify(camisetaTalleRepository, never()).save(variante);
    }

    @Test
    void cancelledOrderCannotChangeState() {
        Pedido pedido = pedidoConUnaVariante("CANCELADO", 10, 3);
        PedidoEstadoUpdateRequest request = estado("ENVIADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(
                com.uade.tpo.marketplace.exception.BusinessException.class,
                () -> pedidoService.updateEstado(1L, request)
        );

        assertEquals("CANCELADO", pedido.getEstado());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void checkoutWithLastUnitCreatesOrderAndLeavesStockAtZero() {
        Usuario usuario = new Usuario();
        Camiseta camiseta = new Camiseta();
        camiseta.setActivo(true);
        camiseta.setNombre("Camiseta prueba");
        camiseta.setPrecio(BigDecimal.TEN);
        camiseta.setTipoCamiseta(new TipoCamiseta("Titular", true, false));

        Talle talle = new Talle();
        talle.setNombre("M");
        CamisetaTalle variante = new CamisetaTalle(camiseta, talle, 1, "SKU-1", "Azul");

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);
        item.setVariante(variante);
        item.setCantidad(1);
        carrito.getItems().add(item);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(carrito));
        when(camisetaTalleRepository.findByIdForUpdate(null)).thenReturn(Optional.of(variante));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        pedidoService.createFromCarrito(1L);

        assertEquals(0, variante.getStock());
        assertEquals(0, carrito.getItems().size());
        verify(camisetaTalleRepository).save(variante);
        verify(carritoRepository).save(carrito);
    }

    private PedidoEstadoUpdateRequest estado(String estado) {
        PedidoEstadoUpdateRequest request = new PedidoEstadoUpdateRequest();
        request.setEstado(estado);
        return request;
    }

    private Pedido pedidoConUnaVariante(String estado, int stock, int cantidad) {
        Usuario usuario = new Usuario();
        Camiseta camiseta = new Camiseta();
        Talle talle = new Talle();
        CamisetaTalle variante = new CamisetaTalle(camiseta, talle, stock, "SKU-1", "Azul");

        DetallePedido detalle = new DetallePedido();
        detalle.setVariante(variante);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(BigDecimal.TEN);
        detalle.setSubtotal(BigDecimal.TEN.multiply(BigDecimal.valueOf(cantidad)));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(estado);
        pedido.setTotal(detalle.getSubtotal());
        pedido.getDetalles().add(detalle);

        return pedido;
    }
}
