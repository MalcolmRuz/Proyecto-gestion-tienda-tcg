package com.gestion.tienda.tcg.pedido.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetallePedidoResponse {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double precioTotal;
}