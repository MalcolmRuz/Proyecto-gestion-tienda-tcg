package com.gestion.tienda.tcg.pedido.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarritoDto {

    private Long idCarrito;
    private String estadoCarrito;
    private Double totalCarrito;
    private List<CarritoItemDto> items;
}