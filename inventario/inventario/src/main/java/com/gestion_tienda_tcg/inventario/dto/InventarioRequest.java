package com.gestion_tienda_tcg.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequest {

@NotBlank(message = "Debe ingresar una fecha valida")
    private LocalDate fechaInventario;
@NotBlank
    private int StockActual;
}
