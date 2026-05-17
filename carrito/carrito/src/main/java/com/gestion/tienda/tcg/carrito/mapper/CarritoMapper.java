package com.gestion.tienda.tcg.carrito.mapper;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarritoMapper {
    private final CarritoItemMapper itemMapper;
    private final CarritoHistorialMapper historialMapper;

    //=========================
    //Solicitar variables de la entidad Carrito
    //=========================
    public Carrito toEntity (CarritoRequest request) {
        return new Carrito();
    }

    //=========================
    //Metodos para listar items e historial de carritos
    //=========================
    public CarritoResponse toResponse (Carrito carrito) {
        List<CarritoItemResponse> items = null;
        if (carrito.getItems() != null){
           items = carrito.getItems()
                   .stream()
                   .map(itemMapper::toResponse)
                   .collect(Collectors.toList());
        }

        List<CarritoHistorialResponse> historial = null;
        if (carrito.getHistorial() != null){
            historial = carrito.getHistorial()
                    .stream()
                    .map(historialMapper::toResponse)
                    .collect(Collectors.toList());
        }
        return new CarritoResponse(
                carrito.getIdCarrito(),
                carrito.getEstadoCarrito(),
                carrito.getTotalCarrito(),
                items,
                historial);
    }
}
