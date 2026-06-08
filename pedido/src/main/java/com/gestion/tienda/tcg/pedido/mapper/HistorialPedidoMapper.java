package com.gestion.tienda.tcg.pedido.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;

@Component
public class HistorialPedidoMapper {

    public HistorialPedidoResponse toResponse(HistorialPedido historial) {
        if (historial == null)
            return null;

        return HistorialPedidoResponse.builder()
                .estadoPedido(historial.getEstadoPedido())
                .descripcion(historial.getDescripcion())
                .fechaCambio(historial.getFechaCambio())
                .build();
    }
}