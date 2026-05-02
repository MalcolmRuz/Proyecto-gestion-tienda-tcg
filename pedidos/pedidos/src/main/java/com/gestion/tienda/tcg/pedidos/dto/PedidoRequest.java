package com.gestion.tienda.tcg.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;
}
