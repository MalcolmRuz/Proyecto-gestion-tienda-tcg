package com.gestion.tienda.tcg.pedidos.dto;

import lombok.Data;

@Data
public class CarritoItemDto {

    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
}