package com.gestion_tienda_tcg.inventario.dto;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequest {

@NotNull
@Min(value = 0, message = "El stock no puede ser menor a 0")
    private Integer stockActual;
@NotNull
    private Long idProducto;
}
