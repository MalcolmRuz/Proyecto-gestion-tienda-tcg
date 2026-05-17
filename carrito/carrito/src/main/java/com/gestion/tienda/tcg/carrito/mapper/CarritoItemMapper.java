package com.gestion.tienda.tcg.carrito.mapper;

import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;
import org.springframework.stereotype.Component;

@Component
public class CarritoItemMapper {
    //=========================
    //Modifica variables traidas de la entidad CarritoItem
    //=========================
    public CarritoItem toEntity(CarritoItemRequest request){
        CarritoItem item = new CarritoItem();

        item.setCantidad(request.getCantidad());
        item.setProductoId(request.getProductoId());

        return item;
    }

    //=========================
    //Solicitar variables de la entidad CarritoItem
    //=========================

    public CarritoItemResponse toResponse(CarritoItem item){
        return new CarritoItemResponse(
                item.getIdItem(),
                item.getProductoId(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getPrecioTotalItem()
        );
    }
}
