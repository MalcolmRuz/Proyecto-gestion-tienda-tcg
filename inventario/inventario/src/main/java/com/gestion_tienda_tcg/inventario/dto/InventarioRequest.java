package com.gestion_tienda_tcg.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequest {

@NotNull(message = "Debe ingresar una fecha valida")
    private LocalDate fechaInventario;
@NotNull
@Min(value = 0, message = "El stock no puede ser menor a 0")
    private Integer stockActual;
@NotNull
    private Long idProducto;
}
