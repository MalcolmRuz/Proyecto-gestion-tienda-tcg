package com.gestion.tienda.tcg.pedido.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.model.DetallePedido;

@Component
public class DetallePedidoMapper {

    public DetallePedidoResponse toResponse(DetallePedido detalle) {
        if (detalle == null)
            return null;

        return DetallePedidoResponse.builder()
                .productoId(detalle.getProductoId())
                .nombreProducto(detalle.getNombreProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .precioTotal(detalle.getPrecioTotal())
                .build();
    }
}