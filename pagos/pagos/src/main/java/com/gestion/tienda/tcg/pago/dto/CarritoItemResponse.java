package com.gestion.tienda.tcg.pago.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItemResponse {

    private Long idItem;
    private Long productoId;
    private String descripcionProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double precioTotalItem;
    private Long idCarrito;
}
