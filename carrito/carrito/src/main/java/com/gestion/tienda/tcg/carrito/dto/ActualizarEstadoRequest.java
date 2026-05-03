package com.gestion.tienda.tcg.carrito.dto;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoCarrito estado;
}