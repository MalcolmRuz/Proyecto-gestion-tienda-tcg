package com.gestion.tienda.tcg.pedido.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.model.Pedido;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null)
            return null;

        return PedidoResponse.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .totalPedido(pedido.getTotalPedido())
                .direccionEnvio(pedido.getDireccionEnvio())
                .estado(pedido.getEstado())
                //.estadoEnvio(pedido.getEstadoEnvio()) // Ya no requiere buscar un objeto Envio
                .detalles(pedido.getDetalles().stream()
                        .map(d -> DetallePedidoResponse.builder()
                                .productoId(d.getProductoId())
                                .nombreProducto(d.getNombreProducto())
                                .cantidad(d.getCantidad())
                                .precioUnitario(d.getPrecioUnitario())
                                .precioTotal(d.getPrecioTotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}