package com.gestion.tienda.tcg.pedidos.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedidos.dto.DetallePedidoRequest;
import com.gestion.tienda.tcg.pedidos.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedidos.model.DetallePedido;

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