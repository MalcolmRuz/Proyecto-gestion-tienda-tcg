package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class CarritoItemDto {
    private Long productoId;
    private String descripcionProducto;
    private Integer cantidad;
    private Double precioUnitario;
}