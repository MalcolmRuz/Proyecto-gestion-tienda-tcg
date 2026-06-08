package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private Double precio;
}