package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class ProductoDto {

    private Long idProducto;

    private String nombreProducto;

    private Double precio;

    private Integer stock;
}
