package com.gestion.tienda.tcg.carrito.mapper;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;
import org.springframework.stereotype.Component;

@Component
public class CarritoHistorialMapper {
    public CarritoHistorialResponse toResponse(CarritoHistorial historial) {
        CarritoHistorialResponse response = new CarritoHistorialResponse();

        response.setIdHistorial(historial.getIdHistorial());
        response.setEstado(historial.getEstado());
        response.setDescripcion(historial.getDescripcion());
        response.setFecha(historial.getFecha());

        return response;
    }
}
