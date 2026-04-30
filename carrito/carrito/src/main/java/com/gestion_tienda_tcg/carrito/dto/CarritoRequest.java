package com.gestion_tienda_tcg.carrito.dto;

import com.gestion_tienda_tcg.carrito.enums.EstadoCarrito;

import lombok.Data;

@Data
public class CarritoRequest {
    private EstadoCarrito estadoCarrito;
}
