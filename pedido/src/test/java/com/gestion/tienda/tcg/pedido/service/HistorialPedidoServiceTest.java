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

import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.mapper.HistorialPedidoMapper;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;
import com.gestion.tienda.tcg.pedido.repository.HistorialPedidoRepository;

@ExtendWith(MockitoExtension.class)
class HistorialPedidoServiceTest {

    @Mock private HistorialPedidoRepository repository;
    @Mock private HistorialPedidoMapper mapper;

    @InjectMocks
    private HistorialPedidoService historialPedidoService;

    @Test
    void obtenerHistorial_deberiaRetornarHistorial_siPedidoExiste() {
        HistorialPedido historial = new HistorialPedido();
        HistorialPedidoResponse response = HistorialPedidoResponse.builder()
            .estadoPedido(EstadoPedido.PAGADO)
            .descripcion("Pedido creado correctamente.")
            .build();

        when(repository.findByPedidoId(1L)).thenReturn(List.of(historial));
        when(mapper.toResponse(any(HistorialPedido.class))).thenReturn(response);

        List<HistorialPedidoResponse> result = historialPedidoService.obtenerHistorial(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(EstadoPedido.PAGADO, result.get(0).getEstadoPedido());
        verify(repository).findByPedidoId(1L);
    }

    @Test
    void obtenerHistorial_deberiaRetornarListaVacia_siPedidoSinHistorial() {
        when(repository.findByPedidoId(99L)).thenReturn(Collections.emptyList());

        List<HistorialPedidoResponse> result = historialPedidoService.obtenerHistorial(99L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
