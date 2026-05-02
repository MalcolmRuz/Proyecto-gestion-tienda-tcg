package com.gestion.tienda.tcg.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItemResponse {

    private Long idItem;
    private Integer cantidad;
    private Double precioUnitario;
    private Double precioTotalItem;
}