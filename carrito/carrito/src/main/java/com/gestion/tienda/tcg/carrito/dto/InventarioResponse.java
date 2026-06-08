package com.gestion.tienda.tcg.carrito.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InventarioResponse {
    private Long idInventario;

    @JsonProperty("idProducto")
    private Long productoId;

    private Integer stockActual;
}