package com.gestion_tienda_tcg.productos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotNull
    private Boolean estado;

    private Long idProveedor;

    private Double precioUnitario;
}
