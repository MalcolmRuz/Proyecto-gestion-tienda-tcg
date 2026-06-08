package com.gestion.tienda.tcg.carrito.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;

@Component
public class CarritoItemMapper {
    // =========================
    // Solicitar variables de la entidad CarritoItem
    // =========================

    public CarritoItemResponse toResponse(CarritoItem item) {
        return new CarritoItemResponse(
                item.getIdItem(),
                item.getProductoId(),
                item.getDescripcionProducto(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getPrecioTotalItem(),
                item.getCarrito().getIdCarrito());
    }
}
