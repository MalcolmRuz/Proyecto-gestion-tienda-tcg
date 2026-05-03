package com.gestion.tienda.tcg.carrito.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;

@Component
public class CarritoItemMapper {

    // Request -> Entity
    public CarritoItem toEntity(
            CarritoItemRequest request) {

        CarritoItem item = new CarritoItem();

        item.setCantidad(request.getCantidad());
        item.setProductoId(request.getProductoId());

        return item;
    }

    // Entity -> Response
    public CarritoItemResponse toResponse(
            CarritoItem item) {

        return new CarritoItemResponse(

                item.getIdItem(),
                item.getProductoId(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getPrecioTotalItem());
    }
}