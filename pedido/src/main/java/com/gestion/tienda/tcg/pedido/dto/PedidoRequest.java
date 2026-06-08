package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class PedidoRequest {
    private Long idCarrito;
    private Long usuarioId;
    private String direccionEnvio;
}