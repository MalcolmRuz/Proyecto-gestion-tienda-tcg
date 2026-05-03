package com.gestion.tienda.tcg.pedidos.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedidos.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedidos.model.HistorialPedido;

@Component
public class HistorialPedidoMapper {

    public HistorialPedidoResponse toResponse(HistorialPedido historial) {

        return new HistorialPedidoResponse(
                historial.getIdHistorial(),
                historial.getEstadoPedido(),
                historial.getFecha());
    }
}