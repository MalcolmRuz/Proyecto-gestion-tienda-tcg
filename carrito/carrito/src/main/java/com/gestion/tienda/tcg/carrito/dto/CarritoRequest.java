package com.gestion.tienda.tcg.carrito.dto;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;

import lombok.Data;

@Data
public class CarritoRequest {
    private EstadoCarrito estadoCarrito;
}
