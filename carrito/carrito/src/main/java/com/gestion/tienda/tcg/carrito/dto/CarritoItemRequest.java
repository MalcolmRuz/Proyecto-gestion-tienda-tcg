package com.gestion.tienda.tcg.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarritoItemRequest {

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    // FK lógica al microservicio producto
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
}