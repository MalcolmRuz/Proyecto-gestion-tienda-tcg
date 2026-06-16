package com.gestion.tienda.tcg.pedido.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.tienda.tcg.pedido.Client.CarritoClient;
import com.gestion.tienda.tcg.pedido.dto.CarritoDto;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.exception.BadRequestException;
import com.gestion.tienda.tcg.pedido.exception.PedidoNotFoundException;
import com.gestion.tienda.tcg.pedido.mapper.PedidoMapper;
import com.gestion.tienda.tcg.pedido.model.DetallePedido;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;
import com.gestion.tienda.tcg.pedido.model.Pedido;
import com.gestion.tienda.tcg.pedido.repository.DetallePedidoRepository;
import com.gestion.tienda.tcg.pedido.repository.HistorialPedidoRepository;
import com.gestion.tienda.tcg.pedido.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private DetallePedidoRepository detallePedidoRepository;
    @Mock
    private HistorialPedidoRepository historialPedidoRepository;
    @Mock
    private PedidoMapper pedidoMapper;
    @Mock
    private CarritoClient carritoClient;

    @InjectMocks
    private PedidoService pedidoService;

    // =====================================================================
    // Helpers
    // =====================================================================

    private CarritoDto carritoConEstado(String estado) {
        CarritoDto carrito = new CarritoDto();
        carrito.setEstadoCarrito(estado);
        carrito.setTotalCarrito(500.0);

        CarritoDto.ItemDto item = new CarritoDto.ItemDto();
        item.setProductoId(1L);
        item.setDescripcionProducto("Carta TCG");
        item.setCantidad(2);
        item.setPrecioUnitario(250.0);

        carrito.setItems(List.of(item));
        return carrito;
    }

    private Pedido pedidoGuardado() {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setUsuarioId(10L);
        p.setIdCarrito(1L);
        p.setEstado(EstadoPedido.PAGADO);
        p.setTotalPedido(500.0);
        p.setDetalles(List.of(new DetallePedido()));
        p.setHistorial(Collections.emptyList());
        return p;
    }

    // =====================================================================
    // crearPedido
    // =====================================================================

    @Test
    void crearPedido_deberiaLanzarExcepcion_siCarritoNoEstaPagado() {
        when(carritoClient.obtenerCarrito(1L)).thenReturn(carritoConEstado("ACTIVO"));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> pedidoService.crearPedido(1L, 10L, "Calle Falsa 123"));

        assertEquals("El carrito debe estar PAGADO", exception.getMessage());
    }

    @Test
    void crearPedido_deberiaCrearPedidoConDetallesYHistorial_siCarritoEstaPagado() {
        CarritoDto carrito = carritoConEstado("PAGADO");
        Pedido guardado = pedidoGuardado();

        when(carritoClient.obtenerCarrito(1L)).thenReturn(carrito);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(guardado);
        when(detallePedidoRepository.saveAll(anyList())).thenReturn(List.of(new DetallePedido()));
        when(historialPedidoRepository.save(any(HistorialPedido.class))).thenReturn(new HistorialPedido());

        PedidoResponse response = PedidoResponse.builder()
                .id(1L)
                .estado(EstadoPedido.PAGADO)
                .totalPedido(500.0)
                .build();
        when(pedidoMapper.toResponse(guardado)).thenReturn(response);

        PedidoResponse result = pedidoService.crearPedido(1L, 10L, "Calle Falsa 123");

        assertNotNull(result);
        assertEquals(EstadoPedido.PAGADO, result.getEstado());
        assertEquals(500.0, result.getTotalPedido());

        verify(pedidoRepository).save(any(Pedido.class));
        verify(detallePedidoRepository).saveAll(anyList());
        verify(historialPedidoRepository).save(any(HistorialPedido.class));
    }

    // =====================================================================
    // actualizarEstadoPedido
    // =====================================================================

    @Test
    void actualizarEstadoPedido_deberiaLanzarExcepcion_siPedidoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        PedidoNotFoundException exception = assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.actualizarEstadoPedido(99L, EstadoPedido.ENVIADO));

        assertEquals("Pedido no encontrado", exception.getMessage());
    }

    // =====================================================================
    // listarTodos
    // =====================================================================

    @Test
    void listarTodos_deberiaRetornarListaDePedidos() {
        Pedido pedido = pedidoGuardado();
        PedidoResponse response = PedidoResponse.builder().id(1L).build();

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(response);

        List<PedidoResponse> result = pedidoService.listarTodos();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    // =====================================================================
    // buscarPorId
    // =====================================================================

    @Test
    void buscarPorId_deberiaLanzarExcepcion_siPedidoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        PedidoNotFoundException exception = assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.buscarPorId(99L));

        assertEquals("Pedido no encontrado", exception.getMessage());
    }
}
