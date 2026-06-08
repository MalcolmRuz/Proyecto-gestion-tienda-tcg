package com.gestion.tienda.tcg.pedido.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnvioRequest {

    @NotBlank(message = "La dirección es obligatoria")
    private String direccionEnvio;
}