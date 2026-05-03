package com.gestion.tienda.tcg.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El carrito es obligatorio")
    private Long carritoId;

    @Valid
    @NotNull(message = "El envío es obligatorio")
    private EnvioRequest envio;
}