package com.gestion.tienda.tcg.pedido.service;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.mapper.DetallePedidoMapper;
import com.gestion.tienda.tcg.pedido.model.DetallePedido;
import com.gestion.tienda.tcg.pedido.repository.DetallePedidoRepository;

@ExtendWith(MockitoExtension.class)
class DetallePedidoServiceTest {

    @Mock private DetallePedidoRepository repository;
    @Mock private DetallePedidoMapper mapper;

    @InjectMocks
    private DetallePedidoService detallePedidoService;

    @Test
    void listarPorPedido_deberiaRetornarDetalles_siPedidoTieneItems() {
        DetallePedido detalle = new DetallePedido();
        DetallePedidoResponse response = DetallePedidoResponse.builder()
            .productoId(1L)
            .cantidad(2)
            .precioUnitario(100.0)
            .precioTotal(200.0)
            .build();

        when(repository.findByPedidoId(1L)).thenReturn(List.of(detalle));
        when(mapper.toResponse(any(DetallePedido.class))).thenReturn(response);

        List<DetallePedidoResponse> result = detallePedidoService.listarPorPedido(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(200.0, result.get(0).getPrecioTotal());
        verify(repository).findByPedidoId(1L);
    }

    @Test
    void listarPorPedido_deberiaRetornarListaVacia_siPedidoSinDetalles() {
        when(repository.findByPedidoId(99L)).thenReturn(Collections.emptyList());

        List<DetallePedidoResponse> result = detallePedidoService.listarPorPedido(99L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
