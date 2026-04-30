package com.gestion_tienda_tcg.productos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    @NotNull
    private String nombre;
    @NotNull
    private String descripcion;
    @NotNull
    private Boolean estado;
}
