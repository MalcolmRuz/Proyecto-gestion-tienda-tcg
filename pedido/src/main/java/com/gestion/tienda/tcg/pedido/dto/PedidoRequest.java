package com.gestion.tienda.tcg.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El envío es obligatorio")
    private EnvioRequest envio;
}