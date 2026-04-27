package com.gestion_tienda_tcg.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InventarioResponse {
    private final Long idInventario;
    private final Long idProducto;
    private final int stockActual;
    private final LocalDate fechaInventario;
}
