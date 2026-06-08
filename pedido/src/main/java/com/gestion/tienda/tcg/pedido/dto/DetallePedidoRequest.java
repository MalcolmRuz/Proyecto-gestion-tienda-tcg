package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class DetallePedidoRequest {
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
}