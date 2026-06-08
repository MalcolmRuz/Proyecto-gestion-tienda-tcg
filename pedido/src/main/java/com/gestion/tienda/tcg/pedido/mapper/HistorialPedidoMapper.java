package com.gestion.tienda.tcg.pedido.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;

@Component
public class HistorialPedidoMapper {

    public HistorialPedidoResponse toResponse(HistorialPedido historial) {

        return new HistorialPedidoResponse(
                historial.getIdHistorial(),
                historial.getEstadoPedido(),
                historial.getDescripcion(),
                historial.getFecha());
    }
}