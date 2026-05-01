package com.gestion_tienda_tcg.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarritoItemRequest {

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Double precioUnitario;

    // Para conectar con productos
    @NotNull
    private Long productoId;
}
