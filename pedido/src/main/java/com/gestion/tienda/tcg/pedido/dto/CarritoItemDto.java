package com.gestion.tienda.tcg.pedido.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarritoItemDto {

    private Long idItem;
    private Long productoId;
    private String descripcionProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double precioTotalItem;
    private Long idCarrito;

}