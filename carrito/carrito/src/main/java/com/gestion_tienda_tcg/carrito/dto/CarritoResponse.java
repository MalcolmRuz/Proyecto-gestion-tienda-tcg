package com.gestion_tienda_tcg.carrito.dto;

import java.util.List;

import com.gestion_tienda_tcg.carrito.enums.EstadoCarrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponse {

    private Long idCarrito;
    private EstadoCarrito estadoCarrito;
    private Double totalCarrito;

    private List<CarritoItemResponse> items;
}
