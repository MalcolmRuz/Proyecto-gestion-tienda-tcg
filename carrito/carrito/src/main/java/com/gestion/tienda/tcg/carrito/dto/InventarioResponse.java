package com.gestion.tienda.tcg.carrito.dto;

import lombok.Data;

@Data
public class InventarioResponse {
    private Long idInventario;
    private Long productoId;
    private Integer stockDisponible;
}
