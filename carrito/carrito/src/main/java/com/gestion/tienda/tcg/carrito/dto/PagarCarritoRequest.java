package com.gestion.tienda.tcg.carrito.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagarCarritoRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;
    private String direccionEnvio;
}