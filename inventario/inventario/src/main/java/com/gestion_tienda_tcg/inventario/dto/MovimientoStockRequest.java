package com.gestion_tienda_tcg.inventario.dto;

import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoStockRequest {

    @NotNull(message = "El ID de inventario es obligatorio")
    private Long idInventario;

    @NotNull(message = "Debe indicar la cantidad de movimiento")
    @Min(value = 1,message = "La cantidad debe ser al menos 1")
    private Integer cantidadMovimiento;

    @NotNull(message = "Debe indicar si es ENTRADA o Salida")
    private TipoMovimiento tipo;
}
