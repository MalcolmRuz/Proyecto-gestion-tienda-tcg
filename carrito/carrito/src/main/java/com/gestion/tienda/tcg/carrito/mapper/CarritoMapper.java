package com.gestion.tienda.tcg.carrito.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.model.Carrito;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CarritoMapper {

    private final CarritoItemMapper itemMapper;

    public Carrito toEntity(CarritoRequest request) {
        return new Carrito();
    }

    public CarritoResponse toResponse(Carrito carrito) {

        List<CarritoItemResponse> items = null;

        if (carrito.getItems() != null) {

            items = carrito.getItems()
                    .stream()
                    .map(itemMapper::toResponse)
                    .collect(Collectors.toList());
        }

        return new CarritoResponse(
                carrito.getIdCarrito(),
                carrito.getEstadoCarrito(),
                carrito.getTotalCarrito(),
                items);
    }
}