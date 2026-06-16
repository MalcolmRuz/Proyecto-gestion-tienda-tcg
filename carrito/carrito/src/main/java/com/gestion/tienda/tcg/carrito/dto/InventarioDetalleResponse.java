package com.gestion.tienda.tcg.carrito.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventarioDetalleResponse {
    private final Long idInventario;
    private final Long idProducto;
    private final String nombreProducto;
    private final int stockActual;
    private final LocalDateTime fechaInventario;
}
