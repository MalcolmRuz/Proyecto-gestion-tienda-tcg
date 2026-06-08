package com.gestion.tienda.tcg.pedido.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoRequest;
import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.model.DetallePedido;

@Component
public class DetallePedidoMapper {

    public DetallePedido toEntity(DetallePedidoRequest request) {

        DetallePedido detalle = new DetallePedido();

        detalle.setProductoId(request.getProductoId());
        detalle.setCantidad(request.getCantidad());

        return detalle;
    }

    public DetallePedidoResponse toResponse(DetallePedido detalle) {

        return new DetallePedidoResponse(
                detalle.getIdDetalle(),
                detalle.getProductoId(),
                detalle.getNombreProducto(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getPrecioTotal());
    }
}