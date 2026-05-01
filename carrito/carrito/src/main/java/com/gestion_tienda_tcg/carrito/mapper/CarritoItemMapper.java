package com.gestion_tienda_tcg.carrito.mapper;

import org.springframework.stereotype.Component;

import com.gestion_tienda_tcg.carrito.dto.CarritoItemRequest;
import com.gestion_tienda_tcg.carrito.dto.CarritoItemResponse;
import com.gestion_tienda_tcg.carrito.model.CarritoItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CarritoItemMapper {

    // Request Entity
    public CarritoItem toEntity(CarritoItemRequest request) {
        CarritoItem item = new CarritoItem();
        item.setCantidad(request.getCantidad());
        item.setPrecioUnitario(request.getPrecioUnitario());

        return item;
    }

    // Entity Response

    public CarritoItemResponse toResponse(CarritoItem item) {
        return new CarritoItemResponse(
                item.getIdItem(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getPrecioTotalItem()

        );
    }
}
