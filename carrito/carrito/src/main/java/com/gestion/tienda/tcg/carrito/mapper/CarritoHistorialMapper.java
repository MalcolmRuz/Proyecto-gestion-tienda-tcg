package com.gestion.tienda.tcg.carrito.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;

@Component
public class CarritoHistorialMapper {

    public CarritoHistorialResponse toResponse(
            CarritoHistorial historial) {

        CarritoHistorialResponse response = new CarritoHistorialResponse();

        response.setIdHistorial(historial.getIdHistorial());
        response.setEstado(historial.getEstado());
        response.setDescripcion(historial.getDescripcion());
        response.setFecha(historial.getFecha());

        return response;
    }
}