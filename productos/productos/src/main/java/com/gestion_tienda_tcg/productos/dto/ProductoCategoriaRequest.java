package com.gestion_tienda_tcg.productos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCategoriaRequest {
    @NotNull
    private Long idProducto;
    @NotBlank
    private String nombreProducto;

    @NotNull
    private Long idCategoria;
    @NotBlank
    private String nombreCategoria;




}
