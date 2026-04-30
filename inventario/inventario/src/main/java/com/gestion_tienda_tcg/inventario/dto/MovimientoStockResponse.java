package com.gestion_tienda_tcg.inventario.dto;

import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MovimientoStockResponse {
    private final Long idMovimiento;
    private final Integer cantidad;
    private final Long  idInventario;
    private final TipoMovimiento tipoMovimiento;
    private final LocalDateTime fechaMovimiento;


}
