package com.gestion.tienda.tcg.pago.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponse {

    private Long idCarrito;
    private String estadoCarrito;
    private Double totalCarrito;
    private List<CarritoItemResponse> items;
}