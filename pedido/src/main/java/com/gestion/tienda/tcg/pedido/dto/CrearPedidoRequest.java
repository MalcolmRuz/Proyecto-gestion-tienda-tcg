package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class CrearPedidoRequest {
    private Long usuarioId;
    private EnvioRequest envio;
}
