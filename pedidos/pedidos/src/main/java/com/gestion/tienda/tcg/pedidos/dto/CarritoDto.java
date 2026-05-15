package com.gestion.tienda.tcg.pedidos.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarritoDto {

    private Long idCarrito;
    private Double totalCarrito;
    private List<CarritoItemDto> items;
}